
SET LOGDATETIME= AUTO-Backup_%DATE%_%TIME%
SET LOGDATETIME= %LOGDATETIME: =%
SET LOGDATETIME= %LOGDATETIME::=_%
SET LOGDATETIME= %LOGDATETIME:.=_%

mysqldump -u crewlink_db --password="crewlink_db" --routines crewlink_db > %LOGDATETIME%.sql

set /p pathName=Enter DB SCRIPT Name for running (eg. ABC.sql ) :%=%
mysql -u crewlink_db --password="crewlink_db" crewlink_db < %pathName%