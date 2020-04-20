@ECHO OFF
ECHO Congratulations! Your first batch file executed successfully.
cd server
cd public
php -S 192.168.100.96:8001
PAUSE