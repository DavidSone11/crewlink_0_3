echo "Enter FILE Name for Backup (eg. ABC.sql) : "
read pathName
echo "You entered: $pathName"
mysqldump -u crewlink_db --password="crewlink_db" --routines  crewlink_db > $pathName