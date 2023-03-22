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

        int i = 5, j = 0, flag = 0;

        for(int k = 0; k < 4; k++){
            if(board[k][i].getColor().equals(EMPTY)){
                flag = 1;
            }
        }

        if(flag != 1){
            return true;
        }

        while(board[column][i].getColor().equals(EMPTY) && i > 0){
            i--;
        }

        while(j < cards.length){

            i++;
            if(i > 6){ throw new NoSpaceException("Full column"); }
            board[column][i] = cards[j];
            j++;

        }

        return false;

    }

}
