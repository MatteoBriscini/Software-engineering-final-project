package it.polimi.ingsw.Server.Model.GroupGoals;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Shared.Cards.Card;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static it.polimi.ingsw.Shared.Cards.CardColor.EMPTY;



public class StairsPatternGoal extends CommonGoal {

    private final String url = "src/main/json/goal/StairsPatternGoal.json";

    int maxX,maxY;

    public StairsPatternGoal(){
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("StairsPatternGoal: JSON FILE NOT FOUND");
        }
    }


     private void jsonCreate() throws FileNotFoundException {
         String urlConfig = url;
         FileReader fileJsonConfig = new FileReader(urlConfig);

         JsonObject controller = new Gson().fromJson(fileJsonConfig, JsonObject.class);

         this.maxX = controller.get("maxX").getAsInt();
         this.maxY = controller.get("maxY").getAsInt();

     }

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
