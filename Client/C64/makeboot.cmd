@echo off
echo Making rogueboot.prg bootloader program

..\tools\cc65\ca65 -I src -I ip65\inc -o target\rogueboot.o src\rogueboot.s
..\tools\cc65\ld65 target\rogueboot.o -C cfg/c64prg-vortex-boot.cfg ip65/ip65/ip65.lib ip65/drivers/c64rrnet.lib -o target\rboottemp.prg
..\tools\exo\exomizer.exe sfx basic target\rboottemp.prg -o target\rogueboot.prg
