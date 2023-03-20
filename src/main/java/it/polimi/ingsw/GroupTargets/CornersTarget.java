package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

//This class is used to check that the cards at the corners of the player's board have the same colors

public class CornersTarget extends EqualTarget{
    public boolean check(Card[][] board){
        return(this.allEqual(new Card[]{board[0][0], board[0][5], board[4][0], board[4][5]}));
    };

}
