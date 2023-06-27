package it.polimi.ingsw.client.Game;

import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Cards.CardColor;

import static it.polimi.ingsw.shared.Cards.CardColor.*;
import static java.util.Arrays.deepToString;

public class PlayerBoard {

    private Card[][] board;

    private int x;

    private int y;

    /**
     * @param board server board sent to the client
     */
    public PlayerBoard(Card[][] board){
        this.board = board;
        this.x = board.length;
        this.y = board[0].length;
    }

    /**
     * @return a copy of the current board
     */
    public Card[][] getBoard(){
        Card[][] tmpBoard=new Card[x][y];

        for(int x=0;x<this.x;x++) {
            for (int y = 0; y < this.y; y++) {
                tmpBoard[x][y] = new Card(board[x][y].getColor());
            }
        }
        return tmpBoard;
    }

    /**
     * @param column x coordinate of the board
     * @param cards number of cards to be inserted in the board
     * @return true if there is enough space for the card, false if there isn't
     */
    public boolean checkSpace(int column, int cards){
        return board[column][y - cards].getColor() == EMPTY;
    }

    public int getColumns(){
        return x;
    }

    public int getRows(){
        return y;
    }

    public CardColor getColor(int x, int y){
        return board[x][y].getColor();
    }

    /**
     * @param column x coordinate of the board
     * @param cards array of cards to be inserted
     */
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
