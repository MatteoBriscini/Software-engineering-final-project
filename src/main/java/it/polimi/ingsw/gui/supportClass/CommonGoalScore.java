package it.polimi.ingsw.gui.supportClass;
/**
 * this class convert common goal score value into the correct image name
 */
public class CommonGoalScore {
    public static String getImgName(int i){
         return switch(i){
            case 8 ->  "scoring_8.jpg";
            case 6 -> "scoring_6.jpg";
            case 4-> "scoring_4.jpg";
             default -> "scoring_2.jpg";
        };
    }
}
