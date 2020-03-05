docker run -it --name db2 -d --privileged=true -p 50000:50000 -e LICENSE=accept -e DB2INST1_PASSWORD=db2inst1 -e DBNAME=INVDB -v $(pwd):/home -v $(pwd)/database:/database ibmcom/db2
