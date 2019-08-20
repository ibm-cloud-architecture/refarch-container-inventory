docker run -it --name db2 --privileged=true -p 50000:50000 -e LICENSE=accept -e DB2INST1_PASSWORD=db2inst1 -e DBNAME=INVDB -v $(pwd):/database ibmcase/greendb2
