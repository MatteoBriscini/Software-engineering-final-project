package it.polimi.ingsw.gui.supportClass;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.shared.Cards.CardColor;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

public class PrivateGoal {
    private static final String[] personalImgName = {"Personal_Goals1.png", "Personal_Goals2.png", "Personal_Goals3.png", "Personal_Goals4.png", "Personal_Goals5.png", "Personal_Goals6.png", "Personal_Goals7.png", "Personal_Goals8.png", "Personal_Goals9.png", "Personal_Goals10.png", "Personal_Goals11.png", "Personal_Goals12.png"};
    public static String getImgName(PositionWithColor[] pos , JsonArray privateGoals){
        JsonArray x,y, color;
        boolean correct;
        for(int j =0; j<privateGoals.size(); j++){
            correct = true;
            x = ((JsonObject)privateGoals.get(j)).get("x").getAsJsonArray();
            y = ((JsonObject)privateGoals.get(j)).get("y").getAsJsonArray();
            color = ((JsonObject)privateGoals.get(j)).get("color").getAsJsonArray();
            for (int i =0; i< pos.length;i++){
                if(pos[i].getX()!=x.get(i).getAsInt() || pos[i].getY()!=y.get(i).getAsInt() || !pos[i].getColor().equals(CardColor.valueOf(color.get(i).getAsString()))){
                    correct = false;
                    break;
                }
            }
            if(correct) return personalImgName[j];
        }
        return "null";
    }
}
