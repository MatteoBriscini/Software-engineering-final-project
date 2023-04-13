package it.polimi.ingsw.Server.Model.GroupGoals;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.Cards.CardColor;
import it.polimi.ingsw.Shared.JsonSupportClasses.JsonUrl;

import java.io.*;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;

//this class is used to check that there are 8 cards having the same color on the player's board
public class EightEqualsGoal extends CommonGoal {


    private JsonUrl jsonUrl;

    int maxX,maxY,nTiles;

    public EightEqualsGoal(){
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("SquaresGoal: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }
    }

    private void jsonCreate() throws FileNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("playerBoardConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("eightEqualsGoal"));
        if(inputStream1 == null) throw new FileNotFoundException();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));

        JsonObject eight = new Gson().fromJson(bufferedReader1, JsonObject.class);
        JsonObject eightBoard = new Gson().fromJson(bufferedReader, JsonObject.class);

        this.maxX = eightBoard.get("x").getAsInt();
        this.maxY = eightBoard.get("y").getAsInt();
        this.nTiles = eight.get("nTiles").getAsInt();
    }


    public boolean check(Card[][] board){
        int[] color=new int[values().length-1]; //this array is used to count the occurrences of each color (the order is the same of the enum in CardColor)
        int x=0;
        int y=0;
        int i;
        while(true){

            if(board[x][y].getColor().equals(EMPTY)){ //if the cell is empty, the cells above it are empty too
                if(x<maxX-1){ //if the method hasn't checked the last column, it checks the next one
                    x++;
                    y=0;
                }
                else return false; //if the method reach this point, it means that it has checked all the columns without counting 8 cards with the same color
            }
            else{

                for(i=0;i<color.length; i++){
                    if(board[x][y].getColor().equals(CardColor.values()[i])) {
                        color[i]++;
                        break;
                    }
                }

                if(color[i]==nTiles)
                    return true;
                if(y<maxY-1) //if the method hasn't checked all the rows of the curren column, it has to check the next row, otherwise it has to check the next column from the bottom
                    y++;
                else
                    if(x<maxX-1){
                        x++;
                        y=0;
                    }
                    else
                        return false; //if the method reaches this point, it means that the whole board has been checked without reaching the target
            }

        }
    };

}
