package it.polimi.ingsw.PlayerClasses;

import com.google.gson.Gson;
import it.polimi.ingsw.Cards.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class PlayerTarget {

    //Attributes

    private int[] x;
    private int[] y;
    private String[] color;


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


    public int checkTarget(PlayerBoard board){
        int counter = 0;
        Card[][] checkBoard = board.getBoard();
        for(int i = 0; i < 6; i++){
            if(checkBoard[x[i]][y[i]].getColor().toString().equals(color[i])){
                counter += 1;
            }
        }
        switch (counter){
            case 0: return 0;
            case 1: return 1;
            case 2: return 2;
            case 3: return 4;
            case 4: return 6;
            case 5: return 9;
            case 6: return 12;
        }
        return 0;
    }

}
