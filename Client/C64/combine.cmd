@echo off
echo Making roguedata file for TFTP download

rem batch file to pad+combine all Rogue data for TFTP download

..\tools\trunc ..\common\rogue-font.raw 2048
..\tools\trunc ..\common\empty.raw      6144

copy /b /v /y ..\common\rogue-font.raw + ..\common\empty.raw + target\rogue6000.bin target\roguedata

