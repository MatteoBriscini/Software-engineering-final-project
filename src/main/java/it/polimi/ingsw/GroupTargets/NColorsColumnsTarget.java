package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

public class NColorsColumnsTarget extends NElementsCheck {

    public boolean check(Card[][] board){
        int tot=0;
        for(int i=0;i<6;i++) {
            if(this.nColorsCheck(new Card[]{board[i][0], board[i][1], board[i][2], board[i][3], board[i][4], board[i][5]}, 1, 3)) {
                tot++;
                if(tot==3)
                    return true;
            }
        }
        return false;
    }
}

