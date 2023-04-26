package it.polimi.ingsw.Client.Game;

import it.polimi.ingsw.Shared.Cards.Card;
import java.util.ArrayList;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;

public class PlayerBoard {

    private Card[][] board;

    private int x;

    private int y;

    public PlayerBoard(Card[][] board){
        this.board = board;
        this.x = board.length;
        this.y = board[0].length;
    }

    public boolean checkSpace(int column, int cards){

        if(board[column][y-cards].getColor() == EMPTY){return true;}
        return false;

    }

    public void addCard(int column, ArrayList<Card> cards){

        int j = 0;

        for(int i = cards.size(); i > 0; i++){
            board[column][y-i] = cards.get(j);
            j++;
        }

    }

}
