@cls
@echo off

rem path = C:\cc65\bin;%PATH%

cl65 -Oi -t c64 src\lib\ultimate_lib_net.c src\u-rogueclient.c ..\C64\src\defaultscreen.s ..\C64\src\colorlookup.s ..\C64\src\screen.s ..\C64\src\sound.s -o target\u-rogueclient.prg
u64remote 192.168.7.64 load C:\Leif\GitHub\MultiRogueLike\Client\common\fontrogue-font4000.prg
u64remote 192.168.7.64 run target\u-rogueclient.prg