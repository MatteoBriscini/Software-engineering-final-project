
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
[original_rulebook](https://github.com/MatteoBriscini/is23-AM19/blob/master/Deliveries/UML/MyShelfie_Ruleboo_ENG.pdf)

...riassunto regole gioco...

## requirements
[original_requirements](https://github.com/MatteoBriscini/is23-AM19/blob/master/Deliveries/UML/requirements.pdf)

..riassunto dei requisiti... 

## design and implementation choices
1. MODELL && CONTROLLER:
   * most of all game parameters are configurable and saved in JSON file (resources/json/config).
   * added 3 minutes timer for player to make a move.
   * on game creation client can specify the number of players for the specific game, the controller start the game autonomous when it's full, only the game creator can start the game when it isn't full.
   * in case of quit (friendly of not) the player will be marked as offline, his turn will be skipped, it always permitted to a client to rejoin a game.
   * all movement controls are duplicated on client and server, to decrease message on network. if the controller receive an invalid move it will consider data on all client in that game like obsolete or incorrect.
   * controller will send to clients all update by delta, to decrease message weight on network, all game data are send to the clients only in 3 case:
     1. on game start
     2. if data on clients are marked as obsoleted or incorrect
     3. if a player rejoin a game after a quit
2. RMI:
   * implemented blocking que on message from server to client to improve velocity.
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

## setup step

## advance feature 

| Functionality                      | State          |
|------------------------------------|----------------|
| chat                               | :green_circle: |
| multy game                         | :green_circle: |
| resilienzy of client disconnection | :green_circle: |


## Development State

| Functionality                                                      | State          | Current                               | Comment                                                                                                                            |
|--------------------------------------------------------------------|----------------|---------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| class: NColorsGroup                                                | :green_circle: | Matteo Briscini                       | <b>implemented && tested </b><br/> it groups the funcionalities of previouse classes (DifferentTarget,EqualTarget,NElementsTarget) |
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
| SOCKET                                                   | :green_circle: | Matteo Briscini + Riccardo Caprioglio | implemented && partially tested                                                                                                    |    
| GameMaster                                                         | :green_circle: | Davide Arcaini                        | <b>implemented && tested<b>                                                                                                        |
| TUI                                                                | :green_circle: | Luca Castelli + Matteo Briscini                       | partially impremented                                                                                                              |
| GUI                                                                | :green_circle: | Davide Arcaini  + Matteo Briscini                        | WIP                                                                                                                                |
| Lobby                                                              | :green_circle: |  Riccardo Caprioglio                                     | <b>implemented && tested</b>                                                                                                       |