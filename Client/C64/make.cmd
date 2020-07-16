@echo off

call clean
if not exist "target" mkdir target

..\tools\cc65\ca65 -I src -I ip65\inc -o target\rogue.o src\rogue.s
..\tools\cc65\ld65 target\rogue.o -C cfg/c64prg-rogue-6000.cfg ip65/ip65/ip65.lib  ip65/drivers/c64rrnet.lib -m target\rogue6000.map -vm -o target\rogue6000.bin
copy /b /v bin\loadaddress6000.bbb + target\rogue6000.bin target\rogue6000-la.bin
