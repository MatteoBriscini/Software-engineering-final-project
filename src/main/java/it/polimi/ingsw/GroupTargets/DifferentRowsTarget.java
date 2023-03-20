package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

public class DifferentRowsTarget extends DifferentTarget{
    private int validCombo = 0;  //amount of valid row
    public boolean check(Card[][] board){
        int i;
        for (i=0; i<6; i++){
           if (this.different(new Card[]{board[0][i], board[1][i], board[2][i], board[3][i], board[4][i]})) validCombo +=1; //create array and call the method all different, if return true increase validCombo
       }
        return (validCombo > 1); //check if exist enough correct combination
    }
}
