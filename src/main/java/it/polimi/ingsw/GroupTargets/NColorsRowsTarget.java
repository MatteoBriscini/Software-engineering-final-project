package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

public class NColorsRowsTarget extends NElementsCheck {

    public boolean check(Card[][] board){
        int tot=0;
        for(int i=0;i<5;i++) {
            if(this.nColorsCheck(new Card[]{board[0][i], board[1][i], board[2][i], board[3][i], board[4][i]}, 1, 3)) {
                tot++;
                if(tot==4)
                    return true;
            }
        }
        return false;
    }

}
