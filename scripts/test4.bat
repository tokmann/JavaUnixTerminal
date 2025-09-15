@echo off
chcp 65001 >nul
cd /d "%~dp0"
set JAR=..\target\Emulator-1.0-SNAPSHOT.jar
set VFS=..\vfs_examples\deep_tree.xml
set SCRIPT=..\scripts_txt\vfs_test.txt

echo Running emulator with VFS=%VFS% and SCRIPT=%SCRIPT%
java -Dfile.encoding=UTF-8 -jar "%JAR%" --vfs="%VFS%" --script="%SCRIPT%"
pause