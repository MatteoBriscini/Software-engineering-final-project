package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.NoSpaceException;
import it.polimi.ingsw.Exceptions.addPlayerToGameExeception;
import it.polimi.ingsw.JsonSupportClasses.Position;
import it.polimi.ingsw.JsonSupportClasses.PositionWithColor;

import java.util.PrimitiveIterator;
import java.util.concurrent.TimeUnit;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Controller {
    /**
     * parameters
     */
    GameMaster game = new GameMaster();
    int playerNum = 0;
    int currentPlayer = 0;
    boolean alreadyStarted = false;
    boolean endGame = false; //stop playing phase in end game

    private Position[][] allowedPositionMatrix; //matrix of allowed position on main board (download from json file)

    private Position[] allowedPositionArray;    //array of allowed position fill based on main board and player number (in start game method)

    private final long timeout = 180; //wait for player time (in seconds)
    private Thread waitForPlayerResponse;
    private Thread endGameThread;

    public Controller(){
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("Controller: JSON FILE NOT FOUND");
        }
    }

    /**
     * dowlad json file
     * @throws FileNotFoundException if method can't file json file
     */
    private void jsonCreate() throws FileNotFoundException {  //download json data
        String url = "src/main/json/gameConfig.json";
        FileReader fileJson = new FileReader(url);
        Gson gson = new Gson();
        allowedPositionMatrix = gson.fromJson(fileJson, Position[][].class);
    }

    /**
     * for debugging
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    /************************************************************************
    ************************************************** start game method ****
    *************************************************************************/
    synchronized public void addNewPlayer(String playerID) throws addPlayerToGameExeception {
        if(playerNum <= 4 && !alreadyStarted){
            playerNum = game.addNewPlayer(playerID);
        } else {
            if(playerNum > 4) throw new addPlayerToGameExeception("try to add player in full game");
            if(alreadyStarted) throw new addPlayerToGameExeception("try to add player in already started game");
        }
        if (playerNum == 4) this.startGame(game.getPlayerArray().get(0).getPlayerID()); //if the game is full start the game
    }

    synchronized public void startGame(String playerID){
        Random rand = new Random();
        int n = rand.nextInt(9);
        int m = rand.nextInt(6);
        int[] array = new int[4];
        ArrayList<Integer> numberList = new ArrayList<Integer>();
        if(playerNum >= 2 && !alreadyStarted && !endGame && game.getPlayerArray().get(0).getPlayerID().equals(playerID)){
            for (int i=1; i<11; i++) numberList .add(i);
            Collections.shuffle(numberList);

            //set commun goal
            game.setCommonGoal(numberList.get(n));
            game.setCommonGoal(numberList.get(n+1));

            //set private goal
            for (n = 0; n < 4; n++){
                array[n] = numberList.get(m+n);
            }
            game.setPrivateGoal(array);

            //fill main board
            ArrayList<Position> tmp = new ArrayList<>();
            Collections.addAll(tmp, allowedPositionMatrix[0]);
            if (playerNum > 2){
                Collections.addAll(tmp, allowedPositionMatrix[1]);
            }
            if (playerNum == 4){
                Collections.addAll(tmp, allowedPositionMatrix[2]);
            }
            allowedPositionArray = tmp.toArray(new Position[0]);
            game.fillMainBoard(allowedPositionArray);
        }       //aggiungere else ***************************************************
    }


    /*************************************************************************
     ************************************************** end game method ******
     *************************************************************************/
    synchronized public void waitForEndGame() {
        endGameThread = new Thread(() -> {
            while (currentPlayer != 0 && !endGame) ;
            if (!endGame) endGame();
        });
        endGameThread.start();
    }

    synchronized public void endGame(){
        endGame = true;
        endGameThread.interrupt();

        System.out.println("end game");
    }
    /*************************************************************************
     ************************************************** in game method *******
     *************************************************************************/
    synchronized public void takeCard(int column, PositionWithColor[] cards, String playerID){
        if(!endGame && alreadyStarted && cards.length > 0 && cards.length<=3 && game.getPlayerArray().get(currentPlayer).getPlayerID().equals(playerID)){
            waitForPlayerResponse.interrupt();

            //remove the cards from main board
            if (game.removeCards(cards)){}

            //add card to player board
            ArrayList<Card> tmp = new ArrayList<>();
            for (PositionWithColor p : cards){
                tmp.add(new Card(p.getColor()));
            }
            Card[] cardArray = tmp.toArray(new Card[0]);
            try {
                if(game.addCard(column, cardArray, currentPlayer)){
                    this.waitForEndGame();
                }
            } catch (NoSpaceException e) {
                //comunica al client che la pescata e invalida perche ha sforato gli spazi disponibili nella colonna
                return;
            }

            this.turn(); //skip to next player

        } //aggiungere else ***************************************************
    }
    synchronized public void turn(){
        if(endGame) return;         //if game is finish, da completare *******************************

        currentPlayer += 1;
        if(currentPlayer > playerNum) currentPlayer =0;
        waitForPlayerResponse = new Thread(() -> {
            int tmp = currentPlayer;
            System.out.println("test0");
            try {
                Thread.sleep(timeout*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("tmp " +tmp);
            System.out.println("current " + currentPlayer);
            if (currentPlayer == tmp) {            System.out.println("test1");
                turn();}
            //skip to next player
        });
        waitForPlayerResponse.start();
    }
}
