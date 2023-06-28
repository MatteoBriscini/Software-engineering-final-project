package it.polimi.ingsw.gui.supportClass;
/**
 * this class convert common goal data into the correct image name
 */
public class CommonGoal {
    public static String getImgName(int i){
        switch (i){
            case 0 :return "4.jpg";
            case 1 :return "3.jpg";
            case 2 :return "8.jpg";
            case 3 :return "11.jpg";
            case 4 :return "10.jpg";
            case 5 :return "2.jpg";
            case 6 :return "6.jpg";
            case 7 :return "5.jpg";
            case 8 :return "7.jpg";
            case 9 :return "9.jpg";
            case 10 :return "1.jpg";
            default:return "12.jpg";
        }
    }
}
