#!/bin/bash
docker run --name=db2 --privileged=true -p 50000:50000  -e DB2INST1_PASSWORD='db2inst1' -e LICENSE='accept' -v $(pwd):/database -it ibmcom/db2 /bin/bash <<EOF
 su - db2inst1;
 db2start;
 cd sql/inventory;
 ./createDB.sh
exit
EOF
ID=$(docker ps -l|grep db2|awk '{print $1}') 
docker commit --author="IBMCASE" $ID ibmcase/greendb2
docker rm $ID
