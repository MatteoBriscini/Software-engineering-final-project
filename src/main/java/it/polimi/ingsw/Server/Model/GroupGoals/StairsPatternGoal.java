package it.polimi.ingsw.Server.Model.GroupGoals;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.JsonUrl;

import java.io.*;

import static it.polimi.ingsw.Shared.Cards.CardColor.EMPTY;



public class StairsPatternGoal extends CommonGoal {

    int maxX,maxY;
    private JsonUrl jsonUrl;
    public StairsPatternGoal(){
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("StairsPatternGoal: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }
    }


     private void jsonCreate() throws FileNotFoundException {

         InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("playerBoardConfig"));
         if(inputStream == null) throw new FileNotFoundException();
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

         JsonObject stair = new Gson().fromJson(bufferedReader, JsonObject.class);

         this.maxX = stair.get("x").getAsInt()-1;
         this.maxY = stair.get("y").getAsInt()-1;

     }
    @Override
    public boolean check(Card[][] board){
        int x=0;
        int y=maxY;
        while(true){
            if(!(board[x][y].getColor().equals(EMPTY)) && (x==maxX || board[x+1][y].getColor().equals(EMPTY))){
                if(x<maxX){
                    x++;
                    y--;
                }
                else
                    return true;
            } else
                if (x==0 && y==maxY) {
                    y--;
                }
                else{
                    x=0;
                    y=0;
                    while(true){
                        if(!(board[x][y].getColor().equals(EMPTY))){
                            if(y==maxY || board[x][y+1].getColor().equals(EMPTY)){
                                if(x<maxX){
                                    x++;
                                    y++;
                                }
                                else return true;
                            }
                            else{
                                if(x==0 && y==0)
                                    y++;
                                else return false;
                            }
                        }
                        else return false;
                    }
                }
        }
    };

}
