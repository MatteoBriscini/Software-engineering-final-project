package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.JsonSupportClasses.Position;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

//import static jdk.internal.misc.InnocuousThread.newThread;

public class Controller {
    GameMaster game = new GameMaster();
    int playerNum = 0;
    int currentPlayer = 0;
    boolean allreadyStarted = false;

    private Position[][] p;

    public Controller(){
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("Controller: JSON FILE NOT FOUND");
        }
    }
    private void jsonCreate() throws FileNotFoundException {  //download json data
        String url = "src/main/json/gameConfig.json";
        FileReader fileJson = new FileReader(url);
        Gson gson = new Gson();
        p = gson.fromJson(fileJson, Position[][].class);
    }
    public void addNewPlayer(String playerID){
        playerNum = game.addNewPlayer(playerID);
    }

    public void startGame(String playerID){
        Random rand = new Random();
        int n = rand.nextInt(9);
        int m = rand.nextInt(6);
        int array[] = new int[4];
        ArrayList<Integer> numberList = new ArrayList<Integer>();
        if(playerNum >= 2 && !allreadyStarted && game.getPlayerArray().get(0).getPlayerID().equals(playerID)){
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
            Collections.addAll(tmp, p[0]);
            if (playerNum > 2){
                Collections.addAll(tmp, p[1]);
            }
            if (playerNum == 4){
                Collections.addAll(tmp, p[2]);
            }
            Position[] posArray = tmp.toArray(new Position[0]);
            game.fullMainBoard(posArray);
        }
    }

    /*
    public void WaitForEndGame(){
        newThread(() -> {
            while (currentPlayer != 0);
            endGame();
        }).start();
    }
     */

    public void endGame(){
    }

}
