
![alt text](https://github.com/MatteoBriscini/is23-AM19/blob/master/Deliveries/UML/Display_1.jpg)
# ingegneria del software project [2022-2023]
### is23-AM19

Members: <br>
  * Matteo Briscini: *matteo.briscini@mail.polimi.it*
  * Luca Castelli: *luca6.castelli@mail.polimi.it*
  * Davide Arcaini: *davide1.arcaini@mail.polimi.it*
  * Riccardo Caprioglio: *riccardo.caprioglio@mail.polimi.it*

...introduzione sul corso...

## rulebook

resume of the game rules
* **Objective:** The objective of My Shelfie is to create the most aesthetically pleasing and balanced bookshelf by strategically placing and organizing books and decorative items.

* **Components:** The game includes a game board representing a bookshelf, a deck of book cards, decorative item cards, and rule cards.

* **Setup:** Each player starts with a blank bookshelf game board. The book cards and decorative item cards are shuffled separately and placed face-down as draw piles. Each player draws a certain number of book cards and a decorative item card to begin.

* **Turn Structure:** The game is played in turns, with players taking actions one at a time in clockwise order. On your turn, you can perform one of the following actions: <br>
  1. Draw Cards: Draw a book card from the book card draw pile or a decorative item card from the decorative item card draw pile. You can only draw one card per turn. <br>
  2. Place Book: Play a book card from your hand onto your bookshelf. Each book card has specific characteristics, such as genre, color, or size, which affect scoring. <br>
  3. Place Decorative Item: Play a decorative item card from your hand onto your bookshelf. Decorative items enhance the aesthetics of your shelf but do not contribute to scoring. <br>
  4. Rearrange: Move one book card or decorative item card from your shelf to a different location on your bookshelf. You can only rearrange items within your own shelf. <br>
  
* **Scoring:** Scoring occurs at the end of the game. The shelfie is scored based on various criteria, including: <br>
  1. Genre Organization: Books of the same genre placed adjacent to each other on the shelf gain bonus points. <br>
  2. Color Harmony: Books of similar colors placed together create a visually appealing shelf and earn additional points. <br>
  3. Size Balance: A well-balanced arrangement of different-sized books adds to the overall shelf appeal and scores points. <br>
  4. Decorative Items: Each decorative item card adds a certain number of points to the final score. <br>

* **End of Game:** The game ends when all players have placed all their book and decorative item cards on their shelves. Final scoring takes place, and the player with the highest score wins the game.
<br> <br> <br>
[original_rulebook](https://github.com/MatteoBriscini/is23-AM19/blob/master/Deliveries/UML/MyShelfie_Ruleboo_ENG.pdf)


## Project Requirements

   The requirements are divided into two groups: <br>
       Game-specific requirements that relate to the rules and mechanics of the game. <br>
       Game-agnostic requirements that relate to design, technology, or implementation aspects.
       
* **Game-Specific** Requirements: <br>
    The game rules are described in the provided rulebook files.
    Use English for class names, interfaces, methods, variables, comments, and technical documentation.
    The user interface language can be either English or Italian.
    Evaluation can be done based on simplified or complete rules, as specified in the rulebook files.

* **Game-Agnostic Requirements:** <br>
    Implement a distributed client-server system using the Model-View-Controller (MVC) pattern.

* **Server Requirements:** <br>
    Implement game rules using JavaSE. <br>
    Create a single server instance to handle one game (or multiple games if advanced functionality is implemented). <br>
    Allow players to take turns using client instances connected to the server via TCP-IP or RMI. <br>
    Support different technologies for players using different clients. <br>

* **Client Requirements:** <br>
    Implement client functionality using JavaSE. <br>
    Implement a graphical user interface (GUI) using Swing or JavaFX. <br>
    Allow players to choose between a text-based user interface (TUI) or a graphical user interface (GUI). <br>
    Support both Socket and RMI communication technologies. <br>

* **Advanced Features (Optional):** <br>
    Multiple games: Implement the server to handle multiple games simultaneously. <br>
    Persistence: Periodically save the game state on disk for resuming after server crashes. <br>
    Resilience to disconnections: Allow disconnected players to rejoin the game. <br>
    Chat: Enable players to communicate via text messages during the game. <br>
<br> <br>
  [original_requirements](https://github.com/MatteoBriscini/is23-AM19/blob/master/Deliveries/UML/requirements.pdf)

## design and implementation choices
1. MODELL && CONTROLLER:
   * the majority of the game parameters are configurable and saved in JSON files (resources/json/config). <br> the most important one are:
       1. common goal number for game and value
       2. private goal config vale, make possible to add new private goal (similar to existing one) without modify the code.
       3. spot vale, min and max size
       4. player board size
       5. main board size and shape
       6. min and max player in a game
   * the player has a timer of 3 minutes (configurable from json) to make a move.
   * when there is only one player in the game, and no one rejoin in 1 minutes (configurable from json), the game will end.
   * on game creation the client can specify the number of players for the specific game, the controller start the game autonomously when the max player number is reached, only the game creator can start the game when is not reached.
   * if a player quit (friendly or not) the player will be marked as offline, his turn will be skipped. It is always permitted to a client to rejoin a game.
   * all movement controls are duplicated on client and server to decrease message on network. if the controller receive an invalid move it will consider the data on all client in that game like obsolete or incorrect.
   * controller will send to clients all update by delta, to decrease the message weight on the network, all game data are send to the clients only in 3 case:
     1. on game start
     2. if data on clients are marked as obsoleted or incorrect
     3. if a player rejoin a game after a quit
2. RMI:
   * implemented blocking que on message from server to client to improve speed.
   * implemented ping pong to detect client or server disconnection.
   * used command pattern on server.
   * all message use JSON to serialize java classes (used GSON).
3. SOCKET:
   * implemented ping pong to detect client or server disconnection.
   * all message use JSON to serialize java classes, all json are for formats like that:
    ```
    {
      "service": "methodName" //needed for message parsing
      "data": {
          //all parameters need for the method
      }
    }
    ```
   * used java.lang.reflect.Method to simplify message parsing
    ``` 
    getNameMethod = SOCKET.this.getClass().getMethod(methodName, arg0, arg1...);//simplified code respect to real implementation
  
   booleanResponse = (boolean) getNameMethod.invoke(SOCKET.this, arg0, arg1...);
    ```

## setup steps
1. firs off all make sure you have installed on your machine JDK 17 or over and javafx. <br>
   *you can download it from this link*:
   * [jdk_17](https://www.oracle.com/it/java/technologies/downloads/#java17)
   * [javafx](https://openjfx.io/) <br>

2. download the jar file from  [here]().  ...mettere link per il jar definitivo..
3. open a terminal and navigate to the folder were you have saved the jar.

**note**: you can change server ip and port from a jsonfile in the jar folder (json/config/netConfig.json).<br>
this take effect in server configuration but also in clients.
### turn on the server:
in terminal type:
```
java -jar AM19.jar --server
```
if you didn't set it before in the json file you have to change the ip here
### turn on cli (or tui) client:
in terminal type:
```
java -jar AM19.jar --tui
```
if you didn't set it before in the json file you have to change the ip here
### turn on gui client:
in terminal type:
```
java -jar AM19.jar
```
if you didn't set it before in the json file you have to change the ip here (by the sett button)
### probably problem:
to make rmi client work on ubuntu (or other linux distro) you have to change the host name.<br>
install vim and type this in terminal:
```
sudo vim /etc/hosts
```
in this file search the local host ip (127.0.0.1) and replace it with your actual ip.
## advance feature 
more info in the project requirement chapter.

| Functionality                        | State          |
|--------------------------------------|----------------|
| chat                                 | :green_circle: |
| multy game                           | :green_circle: |
| resilienzy from client disconnection | :green_circle: |


## Development State

| Functionality                                                      | State          | Current                               | Comment                                                                                                                         |
|--------------------------------------------------------------------|----------------|---------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| class: NColorsGroup                                                | :green_circle: | Matteo Briscini                       | <b>implemented && tested </b><br/> it groups the funcionalities of previouse classes (DifferentTarget,EqualTarget,NElementsTarget) |
| CornersGoal <br> <b>class</b>: OneColorPatternGoal                 | :green_circle: | Luca Castelli + Matteo Briscini       | <b>implemented && tested</b>                                                                                                    |
| class: EightEqualsTarget                                           | :green_circle: | Luca Castelli                         | implemented && tested                                                                                                           |
| NColorsColumsGoal <br> <b>class</b>: RainbowRowsAndColumnsGoals    | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                    |
| NColorsRowsGoal <br> <b>class</b>: RainbowRowsAndColumnsGoals      | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                    |
| GroupGoals                                                         | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                    |
| class : StairsPattern                                              | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                    |
| DifferentColomnsGoal <br> <b>class</b>: RainbowRowsAndColumnsGoals | :green_circle: | Luca Castelli + Matteo Briscini       | <b>implemented && tested</b>                                                                                                    |
| DifferentRowsGoal  <br> <b>class</b>: RainbowRowsAndColumnsGoals   | :green_circle: | Luca Castelli + Matteo Briscini       | <b>implemented && tested</b>                                                                                                    |
| DiagonAlleyGoal <br> <b>class</b>: OneColorPatternGoal             | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                    |
| CrossTarget   <br> <b>class</b>: OneColorPatternGoal               | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                    |
| CouplesGoal <br> <b>class</b>: CouplesAndPokersGoals               | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                    |
| PokerGoal <br> <b>class</b>: CouplesAndPokersGoals                 | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                    |
| class : Player                                                     | :green_circle: | Davide Arcaini + Riccardo Caprioglio  | <b>implemented && tested</b>                                                                                                    |
| class : SquaresGoal                                                | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                    |
| class : MainBoard                                                  | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                    |
| class : PlayerBoard                                                | :green_circle: | Riccardo Caprioglio                   | <b>implemented && tested</b>                                                                                                    |            
| class : PlayerTarget                                               | :green_circle: | Riccardo Caprioglio                   | <b>implemented && tested</b>                                                                                                    |    
| class : Chat                                                       | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                    |    
| Controller                                                         | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                    |    
| ConnectionControllerManager                                        | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                    |    
| RMI                                                                | :green_circle: | Matteo Briscini + Riccardo Caprioglio | implemented && partially tested                                                                                                 |    
| SOCKET                                                   | :green_circle: | Matteo Briscini + Riccardo Caprioglio | implemented && partially tested                                                                                                 |    
| GameMaster                                                         | :green_circle: | Davide Arcaini                        | <b>implemented && tested<b>                                                                                                     |
| TUI                                                                | :green_circle: | Luca Castelli + Matteo Briscini                       | <b>implemented</b>                                                                                                              |
| GUI                                                                | :green_circle: | Davide Arcaini  + Matteo Briscini                        | <b>implemented</b>                                                                                                                       |
| Lobby                                                              | :green_circle: |  Riccardo Caprioglio                                     | <b>implemented && tested</b>                                                                                                    |
