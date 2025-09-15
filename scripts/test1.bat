@echo off
chcp 65001 >nul
echo Тестовый запуск эмулятора с test1.txt
cd /d "%~dp0"
set JAR=..\target\Emulator-1.0-SNAPSHOT.jar
set VFS=C:\Users\rodya\Desktop\КонфигурационноеУправление\Emulator\vfs
set SCRIPT=..\scripts_txt\test1.txt

echo Running emulator with VFS=%VFS% and SCRIPT=%SCRIPT%
java -Dfile.encoding=UTF-8 -jar "%JAR%" --vfs="%VFS%" --script="%SCRIPT%"
pause
