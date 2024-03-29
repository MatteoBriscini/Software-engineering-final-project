
![alt text](https://github.com/MatteoBriscini/is23-AM19/blob/master/Deliveries/others/Display_1.jpg)
# Software engineering final project

### Politecnico di Milano [2022-2023]

>Members: <br>
>  * Matteo Briscini: *matteo.briscini@mail.polimi.it*
>  * Luca Castelli: *luca6.castelli@mail.polimi.it*
> * Davide Arcaini: *davide1.arcaini@mail.polimi.it*
  >* Riccardo Caprioglio: *riccardo.caprioglio@mail.polimi.it*

This is the final exam of software engineering (a course of third year in Polytechnic of Milan). <br> 
The goal's project is to implement a real board game into a client-server application based on Java and JavaFx, using the theoretically subjects and instruments studied during the course.<br>
In the course students had learnt object orientation programming (in Java), functional programming in Java, principles of structural and functional testing.

[official course program](https://www11.ceda.polimi.it/schedaincarico/schedaincarico/controller/scheda_pubblica/SchedaPublic.do?&evn_default=evento&c_classe=744411&__pj0=0&__pj1=af5130f72fce92f31f70af22602dfa8c)

#### INDEX: <br>
1. [Game's rulebook](#Games-rulebook)
2. [Project Requirements](#Project-Requirements)
3. [Design and implementation choices](#Design-and-implementation-choices)
4. [Testing](#testing-)
5. [Setup steps](#Setup-steps)
   * [server](#turn-on-the-server)
   * [cli client](#turn-on-cli-client)
   * [gui client](#turn-on-gui-client)
   * [Possible problems](#possible-problems)
6. [GUI](#graphical-user-interface)
7. [CLI](#command-line-interface)
8. [Advance feature](#advance-features)
9. [Development State](#development-state)

## Game's rulebook
Resume of the game's rules:
* **Goal:** the goal of My Shelfie is to create the most aesthetically pleasing and balanced bookshelf by strategically placing and organizing books and decorative items.

* **Components:** the game includes a main board, 4 player boards representing a bookshelf, a deck of tiles, goal cards, and a rule card.

* **Setup:** each player starts with a blank bookshelf game board. The book cards and goal cards are shuffled separately and placed face-down as draw piles. Each player draws a certain number of book cards and a goal card to begin.

* **Turn structure:** the game is played in turns, with players taking actions one at a time in clockwise order. On your turn, you can perform one of the following actions: <br>
  1. Draw cards: draw a book card from the main board, in group from one to three, adjacent and placed on the same row or column. <br>
  2. Place book: place the book cards from your hand onto your bookshelf, all in the same column. Each book card has specific characteristics, such as genre, or color, which affect scoring. <br>
  
* **Scoring:** scoring calculation occurs at the end of the game. The shelfie is scored based on various criteria, including: <br>
  1. Genre organization: books of the same type, placed adjacent to each other on the shelf, gain bonus points. <br>
  2. Color harmony: books of same colors, placed together, create a visually appealing shelf and earn additional points. <br>
  3. Goal items: each goal card adds a certain number of points to the final score. <br>

* **End of game:** the game end when one players has completely filled his bookshelf. Final scoring calculation takes place, and the player with the highest score wins the game.
<br> <br> <br>

[original_rulebook](https://github.com/MatteoBriscini/is23-AM19/blob/master/Deliveries/MyShelfie_Ruleboo_ENG.pdf)


## Project Requirements

   The requirements are divided into two groups: <br>
   1. Game specific requirements: relate to the rules and mechanics of the game. <br>
   2. Game agnostic requirements: relate to design, technology, or implementation aspects.
       
* **Game specific requirements:** <br>
    the game rules are described in the provided rulebook file; <br>
    use English for class names, interfaces, methods, variables, comments, and technical documentation; <br>
    yhe project can be done based on simplified or complete game rules, as specified in the rulebook file; <br>

* **Game agnostic requirements:** <br>
    Implement a distributed client-server system using the Model-View-Controller (MVC) pattern.

  * **Server requirements:** <br>
      implement game rules using JavaSE; <br>
      create a single server instance to handle one game (or multiple games if advanced functionality is implemented); <br>
      allow players to take turns using client instances connected to the server via TCP-IP or RMI; <br>
      support different technologies for players using several clients; <br>

  * **Client requirements:** <br>
      implement client functionality using JavaSE; <br>
      allow players to choose between a text-based user interface (TUI) or a graphical user interface (GUI); <br>
      implement a graphical user interface (GUI) using Swing or JavaFX; <br>
      support both Socket and RMI communication technologies; <br>

  * **Advanced features (optional):** <br>
      multiple games: implement the server to handle multiple games simultaneously; <br>
      persistence: periodically save the game state on disk for resuming after server crashes; <br>
      resilience to disconnections: allow disconnected players to rejoin the game; <br>
      chat: enable players to communicate via text messages (private or public) during the game; <br>
  <br> <br>
  
[original_requirements](https://github.com/MatteoBriscini/is23-AM19/blob/master/Deliveries/requirements.pdf)

## Design and implementation choices
1. MODEL && CONTROLLER:
   * The majority of the game parameters are configurable and saved in JSON files [(resources/json/config)](https://github.com/MatteoBriscini/is23-AM19/tree/master/src/main/resources/json/config). <br> The most important one are: 
       1. common goal number for a match and their value
       2. private goal configuration value, making possible to add new private goal (similar to existing one) without modify the code
       3. spots value, min and max size
       4. player board size
       5. main board size and shape
       6. min and max player in a match
   * The player has a 3 minutes timer (configurable from json) to make a move.
   * When there is only one player in the match, and no one rejoin in 1 minutes (configurable from json), the game will be terminated.
   * On game creation the client can specify the number of players for the specific match; the Controller start the game autonomously when the max player number is reached. Only the game creator can start the game when is not full.
   * If a player quit (friendly or not) it will be marked as offline, his turn will be skipped. It is always permitted to a client to rejoin a game.
   * All movement checks are duplicated on client and server to decrease message on network. If the controller receive an invalid move it will consider the data on all clients, in that march, like obsolete or incorrect.
   * Controller will send to clients all update by delta, to decrease the message weight on the network, all game data are sent to all the clients only in 3 cases:
     1. on game start
     2. if data on clients are marked as obsoleted or incorrect
     3. if a player rejoin the game after a quit
2. LOBBY:
   * implemented local sign up and login system using a JSON file saved in a config directory in the user home directory
   * implemented reconnection to game if player disconnects
   * implemented multiple games
   * the player can choose to enter a random game or a game with a selected player
   * the player can create a game with the standard number of players or with a maximum number of players that can join
3. RMI:
   * implemented blocking que on message from server to client to improve the speed.
   * implemented ping pong to detect client or server disconnection.
   * used command pattern on the server, to implement messages.
   * all messages use JSON to serialize java classes (used GSON).
4. SOCKET:
   * implemented ping pong to detect client or server disconnection.
   * all message use JSON to serialize java classes. <br> All JSON are formatted like that:
    ```
    {
      "service": "methodName" //needed for message parsing
      "data": {
          //all parameters need for the method
      }
    }
    ```
   * used java.lang.reflect.Method to simplify message parsing:
    ``` 
    getNameMethod = SOCKET.this.getClass().getMethod(methodName, arg0, arg1...);//simplified code respect to real implementation
  
   booleanResponse = (boolean) getNameMethod.invoke(SOCKET.this, arg0, arg1...);
    ```
## Graphical User Interface
![alt text](https://github.com/MatteoBriscini/is23-AM19/blob/master/Deliveries/others/GuiPresImg.png)

UI isn't only the way as most of the players will play the game, it is the first impression with user and it will represent yours game concept. <br>
Different players have several way to play, and they need a specific UI; so we choose to develop a full configurable GUI, where all not mandatory elements (chat, common and private goal) can be dynamical squeeze to reach the maximus concentration. <br>
Technically the user interface is made with JavaFx and some file of CSS to make it look better. For improving speed (according to javaFx limitation) all updates are created by delta, similar to what was done for the network logic. <br>
Main board and Player board are large clickable elements, the selected tiles or column is deduced by clicked coordinates. When some tiles are selected a new menu is showed, where it is possible to reorder the move, select the column on your player board or reset the entire move. <br>
Don't forget to turn on volume! a relaxing music will accompany you during all your games.

<details>
<summary> 
    Insights into the chat
</summary>
   <br> The advance feature for chat ask to give to players the possibility to send private or public message. In the GUI we maintain trace of all the chat history (including your messages), distinguishing message type by different colors. <br>
    Error and game message will be also showed as messages in the chat. If the chat it's squeezed the error messages will be showed in some popups.
</details>

## Command Line Interface
![alt text](https://github.com/MatteoBriscini/is23-AM19/blob/master/Deliveries/others/TuiPresImg.png)

develop a CLI is match faster than develop and design a GUI, so we had start to develop CLI and GUI in parallel to have as soon as possible a playable version of the game to test it.
the result is a quite nice and playable version of the game from command line.

> **knew problem:**
> if the client receive un update or a chat message during typing the update wil be printed and input command will be broke, we had chose not to solve it.

<details>
    <summary>command list (for in game phase)</summary>

* Commands list
    ``` 
  /help
    ```
* Start the game (only the master can)
    ``` 
  /start
    ```
* Quit from the game
    ``` 
  /quit
    ```
* Type a massage
    ``` 
  /chat "messageBody"  //for broadcast messages
  /chat --"playerName": "messageBody" //for private messages 
    ```
* Hide interface section<br>
parameters list: (board, commonGoal, privateGoal)
    ``` 
      /hide --"param"
    ```
* Show interface section<br>
  parameters list: (board, commonGoal, privateGoal)
    ``` 
      /show --"param"
    ```
* Pick cars (strong syntax)
    ``` 
      /pick [column;x,y;x,y...] //from 1 to 3 tiles
    ```




</details>



## Testing 
All classes and methods on the server are tested (with junit) with limit cases. Connection logic (rmi and socket) is partially tested. Client is fulled tested excepted for GUI or CLI classes and methods.<br>
Globally the test has a Class coverage of 63% (61/96) and a Method coverage of 61% (407/658).<br> Following some interesting statistics.
<details>
<summary> 
    More detailed data
</summary>

| Package or class | Class, % | Class   | Method, % | Method    |
|------------------|----------|---------|-----------|-----------|
|                  |          |         |           |           |
| Lobby            | ---      | ---     | 68%       | 11 / 16   |
| model            | 100%     | 12 / 12 | 96%       | 78 / 81   |
| Controller       | ---      | ---     | 90%       | 48 / 53   |
|                  |          |         |           |           |
| clientController | 100%     | 3 / 3   | 63%       | 35 / 55   |
| remoteView       | 100%     | 2 / 2   | 53%       | 8 / 15    |
|                  |          |         |           |           |
| clientConnection | 100%     | 4 / 4   | 83%       | 65 / 82   |
| serverConnection | 83%      | 20 / 24 | 83%       | 125 / 149 |


</details>

 You can find all test code [here!](https://github.com/MatteoBriscini/is23-AM19/tree/master/src/test/java/it/polimi/ingsw)


## Setup steps
1. First of all make sure you have installed on your machine JDK 17 (or over) and Javafx. <br>
   *You can download it from the following links*:
   * [jdk_17](https://www.oracle.com/it/java/technologies/downloads/#java17)
   * [javafx](https://openjfx.io/) <br>

2. Download the AM19.jar file from  [here](https://github.com/MatteoBriscini/is23-AM19/releases)!
3. Open a terminal and navigate to the folder were you have saved the AM19.jar.

>**Note**: you can change server IP and PORT from a JSON file in the AM19.jar folder (json/config/netConfig.json).<br>
This will have effect in server configuration, but also in clients.
### Turn on the server:
in terminal type:
```
java -jar AM19.jar --server
```
>**Note**: If you didn't set it before in the JSON file you have to change the IP here
### Turn on CLI client:
in terminal type:
```
java -jar AM19.jar --tui
```
>**Note**: If you didn't set it before in the JSON file you have to change the IP here
### Turn on GUI client:
in terminal type:
```
java -jar AM19.jar
```
>**Note**: If you didn't set it before in the JSON file you have to change the IP here (by the settings' button)
### Possible problems:
To make RMI client works on ubuntu (or other linux distro) you have to change the host name.<br>
Install VIM and type this in terminal:
```
sudo vim /etc/hosts
```
In this file search the local-host IP (127.0.0.1) and replace it with your actual IP.
## Advance features
More info in the [project requirement](#project-requirements) chapter.

| Functionality                        | State          |
|--------------------------------------|----------------|
| chat                                 | :green_circle: |
| multi game                           | :green_circle: |
| resiliency from client disconnection | :green_circle: |

## License

My Shelfie is property of Cranio Creations and all of the copyrighted graphical assets used in this project were supplied by Politecnico di Milano in collaboration with their rights' holders.

## Development State

<details>
<summary> 
    All development state
</summary>

| Functionality                                                      | State          | Current                               | Comment                                                                                                                            |
|--------------------------------------------------------------------|----------------|---------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| class: NColorsGroup                                                | :green_circle: | Matteo Briscini                       | <b>implemented && tested </b><br/> it groups the functionalities of previous classes (DifferentTarget,EqualTarget,NElementsTarget) |
| CornersGoal <br> <b>class</b>: OneColorPatternGoal                 | :green_circle: | Luca Castelli + Matteo Briscini       | <b>implemented && tested</b>                                                                                                       |
| class: EightEqualsTarget                                           | :green_circle: | Luca Castelli                         | implemented && tested                                                                                                              |
| NColorsColumsGoal <br> <b>class</b>: RainbowRowsAndColumnsGoals    | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                       |
| NColorsRowsGoal <br> <b>class</b>: RainbowRowsAndColumnsGoals      | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                       |
| GroupGoals                                                         | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                       |
| class : StairsPattern                                              | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                       |
| DifferentColomnsGoal <br> <b>class</b>: RainbowRowsAndColumnsGoals | :green_circle: | Luca Castelli + Matteo Briscini       | <b>implemented && tested</b>                                                                                                       |
| DifferentRowsGoal  <br> <b>class</b>: RainbowRowsAndColumnsGoals   | :green_circle: | Luca Castelli + Matteo Briscini       | <b>implemented && tested</b>                                                                                                       |
| DiagonAlleyGoal <br> <b>class</b>: OneColorPatternGoal             | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                       |
| CrossTarget   <br> <b>class</b>: OneColorPatternGoal               | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                       |
| CouplesGoal <br> <b>class</b>: CouplesAndPokersGoals               | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                       |
| PokerGoal <br> <b>class</b>: CouplesAndPokersGoals                 | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                       |
| class : Player                                                     | :green_circle: | Davide Arcaini + Riccardo Caprioglio  | <b>implemented && tested</b>                                                                                                       |
| class : SquaresGoal                                                | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                       |
| class : MainBoard                                                  | :green_circle: | Luca Castelli                         | <b>implemented && tested</b>                                                                                                       |
| class : PlayerBoard                                                | :green_circle: | Riccardo Caprioglio                   | <b>implemented && tested</b>                                                                                                       |            
| class : PlayerTarget                                               | :green_circle: | Riccardo Caprioglio                   | <b>implemented && tested</b>                                                                                                       |    
| class : Chat                                                       | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                       |    
| Controller                                                         | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                       |    
| ConnectionControllerManager                                        | :green_circle: | Matteo Briscini                       | <b>implemented && tested</b>                                                                                                       |    
| RMI                                                                | :green_circle: | Matteo Briscini + Riccardo Caprioglio | implemented && partially tested                                                                                                    |    
| SOCKET                                                             | :green_circle: | Matteo Briscini + Riccardo Caprioglio | implemented && partially tested                                                                                                    |    
| GameMaster                                                         | :green_circle: | Davide Arcaini                        | <b>implemented && tested<b>                                                                                                        |
| TUI                                                                | :green_circle: | Luca Castelli + Matteo Briscini       | <b>implemented</b>                                                                                                                 |
| GUI                                                                | :green_circle: | Davide Arcaini  + Matteo Briscini     | <b>implemented</b>                                                                                                                 |
| Lobby                                                              | :green_circle: | Riccardo Caprioglio                   | <b>implemented && tested</b>                                                                                                       |

</details>
