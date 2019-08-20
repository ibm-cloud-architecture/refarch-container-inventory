echo "Verify container inventory"
db2 "connect to INVDB"
db2 -x "select containerid,type from containers" |wc -l >> xx
read rows < xx
echo $rows
rm xx


