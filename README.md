# High Seas Rogue Like

Explore the Leimart Sea in this multiplayer retro roguelike. Navigate your ship across the sea,
disembark on islands to battle mobs and find loot, and work together with or battle against other 
seafarers on land and sea.

The server is written in Java 8 and supports real or emulated Commodore 64 clients over the internet.
 
Forked from MultiplayerRogueLike by Leif Bloomquist. Leif's presentation at the 2018 Roguelike Celebration
is what got me interested in starting this project. Thanks Leif!

## Developer Guide

###Tools Used - Server
- JDK 1.8 (Amazon Corretto recommended)
- Maven 3
- Tiled
- IntelliJ Community Edition or other IDE of your choice
- Gimp or other image editor of your choice

### Tools Used - Windows/Commodore 64 cross-platform development
- CC65
- Exomizer
- VICE Emulator

### Developer Setup - Windows
- Add the Java JDK bin directory to the PATH environment variable
- Set JAVA_HOME environment variable to the JDK directory. E.g. set JAVA_HOME=C:\Users\Kevin\.jdks\corretto-1.8.0_252

### Building the Server project
- cd server
- mvn package

### Running the development build of the server
- cd server\target
- java -jar high-seas-server-0.1.0.jar

### Building the C64 project
- cd client\c64
- make
- combine
- makeboot

## Running the C64 game
- drag target\rogueboot.prg onto Vice screen to auto run
- rogueboot will attempt to download roguedata from the rogue server and then jump into the game code $6000





