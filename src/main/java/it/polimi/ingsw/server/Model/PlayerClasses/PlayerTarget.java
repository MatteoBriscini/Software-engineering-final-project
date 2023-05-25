package it.polimi.ingsw.server.Model.PlayerClasses;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PlayerTarget {
    //Attributes
    private final int[] x;
    private final int[] y;
    private final String[] color;
    //json value
    private JsonUrl jsonUrl;
    private boolean config = false;
    private int max;
    private JsonArray pt = new JsonArray();

    public PlayerTarget(int[] x, int[] y, String[] color) {
        this.x = x;

        this.y = y;

        this.color = color;
    }


    //constructor of copies
    public PlayerTarget(PlayerTarget playerTarget){
        this.x = playerTarget.x;
        this.y = playerTarget.y;
        this.color = playerTarget.color;
    }



    //Get methods

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }

    public String[] getColor() {
        return color;
    }


    //Methods
    private void jsonCreate() throws FileNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("playerBoardConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("playerGoalConfig"));
        if(inputStream1 == null) throw new FileNotFoundException();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));

        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.max = jsonObject.get("y").getAsInt();

        jsonObject = new Gson().fromJson(bufferedReader1 , JsonObject.class);
        this.pt = jsonObject.get("pt").getAsJsonArray();
    }

        /**
         * The method checks how many points the Player made based on his PlayerTarget
         * @param board the PlayerBoard to be checked
         * @return the points obtained
         */
    public int checkTarget(PlayerBoard board){
        if(!config) {
            try {
                jsonCreate();
            } catch (FileNotFoundException e) {
                System.out.println("PlayerTarget: JSON FILE NOT FOUND");
                throw new RuntimeException(e);
            }
        }
        int counter = 0;
        Card[][] checkBoard = board.getBoard(); //initialize board for checking

        for(int i = 0; i < max; i++){
            if(checkBoard[x[i]][y[i]].getColor().toString().equals(color[i])){ //checking the color in all the coordinates of the PlayerTarget
                counter += 1;
            }
        }
        return pt.get(counter).getAsInt();
    }

}
