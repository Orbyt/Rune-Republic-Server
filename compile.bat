@echo off
title Rune Republic: Compile
echo ===== COMPILING - PLEASE WAIT... =====
"C:\Program Files\Java\jdk1.8.0_05\bin\javac.exe" -Xlint:none -d bin -cp lib/*; src\com\rs\worldserver\*.java src\com\rs\worldserver\admin\*.java src\com\rs\worldserver\content\*.java src\com\rs\worldserver\content\skill\*.java src\com\rs\worldserver\events\*.java src\com\rs\worldserver\hash\*.java src\com\rs\worldserver\io\*.java src\com\rs\worldserver\model\*.java src\com\rs\worldserver\model\npc\*.java src\com\rs\worldserver\model\object\*.java src\com\rs\worldserver\model\player\*.java src\com\rs\worldserver\model\player\command\*.java src\com\rs\worldserver\model\player\packet\*.java src\com\rs\worldserver\util\*.java src\com\rs\worldserver\util\log\*.java src\com\rs\worldserver\world\*.java
echo ===== DONE ===========================
pause
exit

