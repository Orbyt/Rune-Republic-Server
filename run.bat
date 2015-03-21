@echo off
title Rune Republic : Running
"C:\Program Files\Java\jdk1.8.0_05\bin\java.exe" -server -Xmx1024m -cp ./jython.jar;/MySql/mysql-connector-java-3.0.08-ga-bin.jar -classpath bin;lib/* com.rs.worldserver.Server release gui
pause

