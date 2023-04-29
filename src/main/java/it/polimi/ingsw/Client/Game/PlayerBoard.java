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
    public Card[][] getBoard(){
        Card[][] tmpBoard=new Card[x][y];

        for(int x=0;x<this.x;x++) {
            for (int y = 0; y < this.x; y++) {
                tmpBoard[x][y] = new Card(board[x][y].getColor());
            }
        }
        return tmpBoard;
    }

    public boolean checkSpace(int column, int cards){

        if(board[column][y-cards].getColor() == EMPTY){return true;}
        return false;

    }

    public void addCard(int column,Card[] cards){
        int i = y-1, j = 0;

        while(board[column][i].getColor().equals(EMPTY) && i != 0){
            i--;
        }

        if(!board[column][i].getColor().equals(EMPTY)) i++;

        while(j < cards.length){

            board[column][i] = cards[j];
            j++;
            i++;

        }

    }

}
