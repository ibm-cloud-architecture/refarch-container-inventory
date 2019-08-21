#!/bin/bash
docker exec -it db2 /bin/bash -c "su - db2inst1; cd /home/sql/inventory; ./createDB.sh"
