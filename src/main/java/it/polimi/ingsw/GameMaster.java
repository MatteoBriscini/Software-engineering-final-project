package it.polimi.ingsw;

import it.polimi.ingsw.playerClasses.*;
import it.polimi.ingsw.GroupTargets.*;

import com.google.gson.Gson;    //import for json
import java.io.FileNotFoundException;
import java.io.FileReader;
public class GameMaster {

    private static int playerNum = 0;
    private static boolean activeGame = false;
    private static char[][] validPosArray; //valid position to set card in main board

    private static int currentPlayer = 0;
    private static Player [] playerArray;
    private static MainBoard mainBoard = new MainBoard();
    private static GroupTarget [] groupTargetArray;
    public GameMaster(){ //costructor
    }

    //############################# start game method
    public static void addPlayer(String playerId){  //new player join the game
        if (playerNum<4 && activeGame == false) {
            //playerArray[playerNum] = new Player(playerId);
            playerNum += 1;
        }
        if (playerNum == 4 && activeGame == false) startGame();  //if the game is full the game will start auto
    }
    public static void startGame(){  //start game

    }

    //############################# end game method
    public static void endGame(){          //end game
    }
    private static void endGamePoint(){         //calc end game point

    }

    //############################# game method
    private static void turn (){                                //manage the player turn

    }
    public static void takeCard(int n, char position[][]){      //take card

    }

    private static void updateAllData (){       //update client data

    }
    private static void calcGroupPoint(){       //for each game verify if reach the group target

    }

    //main board usage
    private static void fillBoard(){        //full main board
       // mainBoard.fillBoard(validPosArray); //fill or refill the main board
    }
}
