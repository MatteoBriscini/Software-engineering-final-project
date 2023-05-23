package it.polimi.ingsw.gui.supportClass;

public class Estimate{
    public static int estimateX(double x){  //return the column
        if(x>475 && x < 535) {
            return 8;
        }else if(x>420.5) {
            return 7;
        }else if (x>363) {
            return 6;
        } else if (x>305.4) {
            return 5;
        } else if (x>250.2) {
            return 4;
        }else if(x>196.6){
            return 3;
        } else if (x>138.2) {
            return 2;
        } else if (x>82.2) {
            return 1;
        } else {
            return 0;
        }
    }
    public static int estimateY(double y){ //return the lineY
        if(y>482.5 && y<537.4) {
            return 0;
        }else if(y>425.4) {
            return 1;
        }else if (y>367) {
            return 2;
        } else if (y>311) {
            return 3;
        } else if (y>253.4) {
            return 4;
        }else if(y>197.4){
            return 5;
        } else if (y>140.6) {
            return 6;
        } else if (y>81.4) {
            return 7;
        } else {
            return 8;
        }
    }
}
