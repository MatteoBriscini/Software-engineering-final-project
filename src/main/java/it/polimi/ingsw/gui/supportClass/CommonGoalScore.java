package it.polimi.ingsw.gui.supportClass;

public class CommonGoalScore {
    public static String getImgName(int i){
         return switch(i){
            case 8 ->  "scorig_8.png";
            case 6 -> "scoring_2.png";
            case 4-> "scoring_4.png";
             default -> "scoring_2.png";
        };
    }
}
