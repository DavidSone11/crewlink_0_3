set /p pathName=Enter FILE Name for Backup (eg. ABC.sql) :%=%
@echo %pathName%
mysqldump -u crewlink_db --password="crewlink_db" --routines crewlink_db > %pathName%