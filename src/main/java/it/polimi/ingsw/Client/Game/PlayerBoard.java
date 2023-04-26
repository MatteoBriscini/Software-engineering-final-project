package it.polimi.ingsw.Client.Game;

import it.polimi.ingsw.Shared.Cards.Card;
import java.util.ArrayList;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;

public class PlayerBoard {

    private Card[][] board;

    private int x;

    private int y;

    public PlayerBoard(Card[][] board){

        this.board = new Card[x][y];
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                this.board[i][j] = board[i][j];
            }
        }

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
