package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

public class DifferentColumnsTaget extends DifferentTarget{
    private int validCombo = 0;  //amount of valid Columns
    public boolean check(Card[][] board){
        int i;
        for (i=0; i<5; i++){
            if (this.different(board[i]))validCombo +=1; //create array and call the method all different, if return true increase validCombo
        }
        return (validCombo > 1); //check if exist enough correct combination
    }

}
