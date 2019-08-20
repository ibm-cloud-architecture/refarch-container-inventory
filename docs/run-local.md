# Run Locally

## Running MQ with docker

### Prepare the MQ run time

> __Attention__: Queue manager and queue data are saved in the filesystem. To avoid losing the queue manager and queue data, we use docker volumes. Volumes are attached to containers when they are run and persist after the container is deleted. The following command creates a volume name `qm1data`

```shell
$  docker volume create qm1data
```

The remote MQ clients use a `Channel` to communicate with the MQ manager over the network. We need to create a virtual docker network to support this communication. The command below creates such network:

```shell
$ docker network create mq-brown-network
``` 

The scripts `runMQlocal` uses docker and the IBM MQ docker image to run MQ as a daemon.