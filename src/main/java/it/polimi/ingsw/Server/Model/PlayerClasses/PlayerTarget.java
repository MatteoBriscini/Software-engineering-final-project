package it.polimi.ingsw.Server.Model.PlayerClasses;

import it.polimi.ingsw.Server.Model.Cards.*;
import it.polimi.ingsw.Server.Model.Cards.Card;

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


    /**
     * The method checks how many points the Player made based on his PlayerTarget
     * @param board the PlayerBoard to be checked
     * @return the points obtained
     */
    public int checkTarget(PlayerBoard board){
        int counter = 0;
        Card[][] checkBoard = board.getBoard(); //initialize board for checking

        for(int i = 0; i < 6; i++){
            if(checkBoard[x[i]][y[i]].getColor().toString().equals(color[i])){ //checking the color in all the coordinates of the PlayerTarget
                counter += 1;
            }
        }
        switch (counter){  //returning the correct points earned based on the number of matching cards in the PlayerBoard
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
