# Deploy and run on Openshift

We address here how to deploy the liberty app to Openshift connected to Db2 on IBM Cloud and MQ on IBM Cloud.

## Pre-requisite

* You should have already provisionned Db2 as service on Cloud, if not see [this section](run-local.md#pre-requisites).

* Get a DB2 server or service up and running. You can create a DB2 service on IBM Cloud using the [product documentation instructions](https://cloud.ibm.com/docs/services/Db2onCloud?topic=Db2onCloud-getting-started#getting-started). Get the service credentials. 
* Install DB driver on your local environment by following one of [those instructions](https://cloud.ibm.com/docs/Db2onCloud/connecting?topic=Db2onCloud-dr_pkg#instlng). Basically, open the DB2 console, under the `burger icon` go to Connection Information and select the operating system needed. Set up the environment with the db2profile: `source /Applications/dsdriver/db2profile`. 

## Create tables and load data 

Using the DB2 console, use `Run SQL` feature, to create the `containers` table (create statement in the file `db2/sql/inventory/create-tables.db2`) and load the data using the SQL file (db2/sql/inventory/loaddata.db2)