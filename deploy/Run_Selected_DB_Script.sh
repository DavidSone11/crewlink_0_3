now="$(date +'%d-%m-%Y_%H-%M-%S')"
now="AUTO-Backup_$now"
echo $now

mysqldump -u crewlink_db --password="crewlink_db" --routines crewlink_db > $now.sql

echo "Enter DB SCRIPT Name for running (eg. ABC.sql ) : "
read pathName
echo "You entered: $pathName"
mysql -u crewlink_db --password="crewlink_db" crewlink_db < $pathName 
