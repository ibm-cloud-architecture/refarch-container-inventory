# Streaming PostgreSQL Updates to Kafka with Debezium
This guide will help you get up and running with Kafka Connect to stream PostgreSQL database changes to a Kafka topic. It will guide you through the installation and configuration of Kafka, Kafka Connect, Debezium & PostgreSQL.

**Note: This guide has only been tested using Docker Desktop for Mac. Results may vary using other Kubernetes cluster types.**

This guide assumes that you have the following:
- Access to a Kubernetes Cluster
- Helm (w/ Tiller) installed on the Kubernetes Cluster
- Helm Incubator repository enabled
- cURL, Postman or another HTTP client

If you do not meet the prerequisites, please see the following links:
- [Getting Started with Kubernetes with Docker on Mac](https://rominirani.com/tutorial-getting-started-with-kubernetes-with-docker-on-mac-7f58467203fd)
- [Install Helm](https://helm.sh/docs/using_helm/)
- [Enable Helm Incubator repository](https://github.com/helm/charts#how-do-i-enable-the-incubator-repository)
- [Install Postman](https://www.getpostman.com/downloads/)

## Initial Setup
To keep this tutorial isolated from other application running in your Kubernetes cluster and to cleanup easier, we will create a separate namespace for the new resources.
```
  $ kubectl create namespace kafka-connect-tutorial
```

(Optional) You may configure your Kubernetes context's default namespace to ```kafka-connect-tutorial``` using the following command:
```
  $ kubectl config set-context --current --namespace kafka-connect-tutorial
```

## Install Kafka & Zookeeper
This section will guide you through the installation of Kafka & Zookeeper. You will also deploy a Kafka client pod to interact with the Kafka Cluster, as well as configure 3 Kafka Topics that will be used by Kafka Connect.

1. Install Kafka & Zookeeper to your namespace using the Incubator Helm Chart.
```
  $ helm install --name kafka --namespace kafka-connect-tutorial incubator/kafka --set external.enabled=true
```
2. Deploy a Kafka Connect client container to your cluster by creating a file in your workspace named ```kafka-client-deploy.yaml``` with the following contents:
```
  # kafka-client-deploy.yaml

  apiVersion: v1
  kind: Pod
  metadata:
     name: kafka-client
  spec:
     containers:
     - name: kafka-client
       image: confluentinc/cp-kafka:5.0.1
       command:
         - sh
         - -c
         - "exec tail -f /dev/null"
```

3. Execute the following command to deploy the Kafka Client Pod:
```
$ kubectl create -f kafka-client-deploy.yaml -n kafka-connect-tutorial
```

4. Create the Kafka Connect Topics using the following commands:
```
$ kubectl -n kafka-connect-tutorial exec kafka-client -- kafka-topics --zookeeper kafka-zookeeper:2181 --topic connect-offsets --create --partitions 1 --replication-factor 1
```
```
$ kubectl -n kafka-connect-tutorial exec kafka-client -- kafka-topics --zookeeper kafka-zookeeper:2181 --topic connect-configs --create --partitions 1 --replication-factor 1
```
```
$ kubectl -n kafka-connect-tutorial exec kafka-client -- kafka-topics --zookeeper kafka-zookeeper:2181 --topic connect-status --create --partitions 1 --replication-factor 1
```

## Install Kafka Connect
This section will guide you through the installation of Kafka Connect using the Debezium Kafka Connect Docker Image. As part of this installation, you will create a NodePort service to expose the Kafka Connect API. This service will be available on port 30500 of your cluster nodes. If you are using Docker Desktop, this will be http://localhost:30500.

1. Create a file named ```kafka-connect-deploy.yaml``` in your workspace and add the following contents:
```
  # kafka-connect-deploy.yaml

  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: kafkaconnect-deploy
    labels:
      app: kafkaconnect
  spec:
    replicas: 1
    selector:
      matchLabels:
        app: kafkaconnect
    template:
      metadata:
        labels:
          app: kafkaconnect
      spec:
        containers:
          - name: kafkaconnect-container
            image: debezium/connect:0.10.0.CR1
            readinessProbe:
              httpGet:
                path: /
                port: 8083
            livenessProbe:
              httpGet:
                path: /
                port: 8083
            env:
              - name: BOOTSTRAP_SERVERS
                value: kafka:9092
              - name: GROUP_ID
                value: "1"
              - name: OFFSET_STORAGE_TOPIC
                value: connect-offsets
              - name: CONFIG_STORAGE_TOPIC
                value: connect-configs
              - name: STATUS_STORAGE_TOPIC
                value: connect-status
            ports:
            - containerPort: 8083
  ---
  apiVersion: v1
  kind: Service
  metadata:
    name: kafkaconnect-service
    labels:
      app: kafkaconnect-service
  spec:
    type: NodePort
    ports:
      - name: kafkaconnect
        protocol: TCP
        port: 8083
        nodePort: 30500
    selector:
        app: kafkaconnect
```
2. Deploy Kafka Connect with the following command:
```
  $ kubectl create -f kafka-connect-deploy.yaml --namespace kafka-connect-tutorial
```

## Install PostgreSQL
This section will guide you through the installation of PostgreSQL using the Stable Helm Chart. You will also add some additional configuration for PostgreSQL necessary for Debezium to read the PostgreSQL transaction log. PostgreSQL will be available on port 30600 of your cluster nodes. If you are using Docker Desktop, this will be http://localhost:30600.

1. Create a PostgreSQL configuration necessary for Debezium. Create a file in your workspace named ```extended.conf``` with the following contents:
```
  # extended.conf

  wal_level = logical
  max_wal_senders = 1
  max_replication_slots = 1
```

2. Create a ConfigMap from the ```extended.conf``` file with the following command:
```
  $ kubectl create configmap --namespace kafka-connect-tutorial --from-file=extended.conf postgresql-config
```

3. Install PostgreSQL using the Stable Helm Chart with the following command:
```
  $ helm install --name postgres --namespace kafka-connect-tutorial --set extendedConfConfigMap=postgresql-config --set service.type=NodePort --set service.nodePort=30600 --set postgresqlPassword=passw0rd stable/postgresql
```

## Add Sample Data to PostgreSQL
1. Open a shell in the Postgres container.
```
  $ kubectl exec --namespace kafka-connect-tutorial -it postgres-postgresql-0  -- /bin/sh
```

2. Login to Postgres with the following command, entering the password ```passw0rd``` when prompted.
```
  $ psql --user postgres
```

3. Create a table named ```people```.
```
  $ CREATE TABLE people(id SERIAL PRIMARY KEY,name VARCHAR(20),creationdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
```

4. Insert data into the table.
```
  $ INSERT INTO people (name) VALUES ('Rachel'),('Thomas'),('Benjamin'),('Allison'),('Todd');
```


## Configure the Debezium PostgreSQL connector
This section will show you how to configure the Debezium PostgreSQL connector.

1. Using your HTTP client (cURL shown), make the following request to the Kafka Connect API. This will configure a new Debezium PostgreSQL connector. This connector monitors the ```pgoutput``` stream for operations on the ```people``` table.

  **Note:** If you are not using Docker Desktop, please set ```localhost``` to the hostname/IP of one of your cluster nodes.

  **Note:** If you did not follow the [Add Sample Data to PostgreSQL](#add-sample-data-to-postgresql) section, replace ```"public.containers"``` with the name of your table.
```
curl -X POST \
  http://localhost:30500/connectors \
  -H 'Content-Type: application/json' \
  -d '{
	"name": "people-connector",
	"config": {
	        "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
	        "plugin.name": "pgoutput",
	        "database.hostname": "postgres-postgresql",
	        "database.port": "5432",
	        "database.user": "postgres",
	        "database.password": "passw0rd",
	        "database.dbname": "postgres",
	        "database.server.name": "postgres",
	        "table.whitelist": "public.people"
	  }
}'
```

2. List the Kafka Topics, showing your newly created topic
```
  $ kubectl -n kafka-connect-tutorial exec kafka-client -- kafka-topics --zookeeper kafka-zookeeper:2181 --list
```

3. Tail the Kafka ```postgres.public.people``` topic to show database transactions being written to the topic from Kafka Connect.

  **Note:** Change ```postgres.public.people``` if you are not using the sample database data
```
  $ kubectl -n kafka-connect-tutorial exec kafka-client -- kafka-console-consumer --topic postgres.public.people --from-beginning --bootstrap-server kafka:9092
```

You may continue to make Create, Update and Delete transactions to the ```people``` table, these changes will appear as messages in the Kafka topic.

## Cleanup
This section will help you remove all of the resources created during this tutorial.

1. Delete the Kafka Helm Release
```
  $ helm delete kafka --purge
```

2. Delete the PostgreSQL Helm Release
```
  $ helm delete postgres --purge
```

3. Delete the ```kafka-connect-tutorial``` namespace
```
  $ kubectl delete namespace kafka-connect-tutorial
```

## References

[Debezium Tutorial](https://debezium.io/documentation/reference/0.10/tutorial.html)

[PostgreSQL plugins/configuration required for Debezium](https://debezium.io/documentation/reference/0.10/postgres-plugins.html)
