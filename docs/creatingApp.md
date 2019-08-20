# Creating app from command line

The goal of this article is to go over a test driven and devops approach to develop this service. 

To create the microprofile app we used the ibmcloud CLI. (version 2.2.0 used).

## Requirements

* [Maven](https://maven.apache.org/install.html)
* Java 8: Any compliant JVM should work.
  * [Java 8 JDK from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
  * [Java 8 JDK from IBM (AIX, Linux, z/OS, IBM i)](http://www.ibm.com/developerworks/java/jdk/),
    or [Download a Liberty server package](https://developer.ibm.com/assets/wasdev/#filter/assetTypeFilters=PRODUCT)
    that contains the IBM JDK (Windows, Linux)

## Step 1: Create the foundation

* Login to IBM Cloud, and set the target to an existing organization and space

```
ibmcloud login -a https://cloud.ibm.com  -u <username>
ibmcloud target -o cent@us.ibm.com -s Cognitive
```

* Create the foundation for an app

```
ibmcloud dev create
```

Then select the appropriate options. For our case there are:

    * Backend Service / Web App
    * Java - MicroProfile / Java EE
    * Java Microservice with Eclipse MicroProfile and Java EE
    * IBM DevOps, deploy to Kubernetes containers

## Step 2: Build and  Run

To build and run the application:
1. `mvn install`
1. `mvn liberty:run-server`

To run the application in Docker use the Docker file called `Dockerfile`:

```
```

 If you do not want to install Maven locally you can use `Dockerfile-tools` to build a container with Maven installed.

### Endpoints

The application exposes the following endpoints:
* Health endpoint: `<host>:<port>/<contextRoot>/health`

The context root is set in the `src/main/webapp/WEB-INF/ibm-web-ext.xml` file. The ports are set in the pom.xml file and exposed to the CLI in the cli-config.yml file.
