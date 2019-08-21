#!/bin/bash

DBNAME="INVDB"
# Get the list of databases
invdb=$(db2 list db directory | grep Indirect -B 5 |grep "Database alias" |awk {'print $4'} |grep $DBNAME)
echo $invdb
if [ -z "$invdb" ] ; then
   echo "DB: $DBNAME not found so let create it"
   db2 create database $DBNAME
fi
db2 connect to $DBNAME
table=$(db2 -x "select containerid,type from containers" )

if [[ "$table" =~ "undefined" ]]
then
  echo "Containers table not found so let create it"
  db2 -tvf create-tables.db2 -z mydb.log
fi

db2 "connect to $DBNAME"
db2 -x "select containerid,type from containers" |wc -l >> xx
read rows < xx
echo $rows
rm xx
if [[ $rows -eq 0 ]]; then
 echo "There is no data in inventory DB, let add some..."
 db2 -tvf loaddata.db2 -z mydb.log
else
  if [[ $rows -lt 9 ]];then
     echo "You do not have all the records"
  else
    echo "Good your db is up and populated"
  fi
fi
