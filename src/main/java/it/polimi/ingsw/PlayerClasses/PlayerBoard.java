package it.polimi.ingsw.PlayerClasses;


import it.polimi.ingsw.Cards.*;
import static it.polimi.ingsw.Cards.CardColor.EMPTY;

public class PlayerBoard {


    //Attributes

    private static Card[][] board;


    //Constructor

    public PlayerBoard(){
        board = new Card[5][6];
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 6; j++){
                board[i][j] = new Card(EMPTY);
            }
        }
    }

    //Get Methods

    public static Card[][] getBoard() {
        return board;
    }


    //Methods

    /**
     * @param column index of column for the player board
     * @param cards array of Card objects
     * @return true if player board is full, false in all other cases
     * @throws NoSpaceException if the selected column is full or there is not enough space for the selected number of cards
     */
    public boolean addCard (int column, Card[] cards) throws NoSpaceException{

        int i = 5, j = 0;
        boolean flag = false;

        while(board[column][i].getColor().equals(EMPTY) && i != 0){
            i--;
        }

        if(!board[column][i].getColor().equals(EMPTY)) i++;

        while(j < cards.length) {

            if (i > 5) {
                throw new NoSpaceException("Full column");
            }
            j++;
            i++;

        }

        j = 0;
        i -= cards.length;

        while(j < cards.length){

            board[column][i] = cards[j];
            j++;
            i++;

        }

        for(int k = 0; k < 5; k++){
            if(board[k][5].getColor().equals(EMPTY)){
                flag = true;
            }
        }
        return !flag;

    }

}
