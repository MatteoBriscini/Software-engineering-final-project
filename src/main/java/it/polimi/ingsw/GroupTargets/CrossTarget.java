package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

public class CrossTarget extends EqualTarget{

    public boolean check(Card[][] board){
        int i, j;
        for (i=1; i<4; i++){ //move from second column to 4th column
            for (j=1; j<6; j++){ //move from second row to 5th column
                if (allEqual(new Card[]{board[i][j],board[i-1][j-1],board[i+1][j+1],board[i-1][j+1],board[i+1][j-1]})) return true;     //call the allEqual method with element of the cross
            }
        }
        return false;
    }

}
