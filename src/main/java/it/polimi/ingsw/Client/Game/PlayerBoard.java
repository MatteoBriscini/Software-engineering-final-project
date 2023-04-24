package it.polimi.ingsw.Client.Game;

import it.polimi.ingsw.Shared.Cards.Card;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;

public class PlayerBoard {

    private Card[][] board;

    private int x;

    private int y;

    public PlayerBoard(Card[][] board){

    }

    public boolean checkSpace(int column, int cards){

        if(board[column][y-cards].getColor() == EMPTY){return true;}
        return false;
    }

}
