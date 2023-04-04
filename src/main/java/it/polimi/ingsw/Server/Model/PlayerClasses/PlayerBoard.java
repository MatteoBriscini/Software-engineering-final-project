package it.polimi.ingsw.Server.Model.PlayerClasses;


import it.polimi.ingsw.Server.Model.Cards.*;
import it.polimi.ingsw.Server.Exceptions.NoSpaceException;
import it.polimi.ingsw.Server.Model.Cards.Card;

import static it.polimi.ingsw.Server.Model.Cards.CardColor.EMPTY;

public class PlayerBoard {


    //Attributes

    private Card[][] board;


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

    public Card[][] getBoard() {
        return board;
    }


    //Methods

    /**
     * Adds cards to the PlayerBoard
     * @param column index of column for the player board
     * @param cards array of Card objects
     * @return true if player board is full, false in all other cases
     * @throws NoSpaceException if the selected column is full or there is not enough space for the selected number of cards
     */
    public boolean addCard (int column, Card[] cards) throws NoSpaceException{

        int i = 5, j = 0;
        boolean flag = false;

        while(board[column][i].getColor().equals(EMPTY) && i != 0){ //check column for correct line start
            i--;
        }

        if(!board[column][i].getColor().equals(EMPTY)) i++; //correcting for empty column

        while(j < cards.length) {

            if (i > 5) {
                throw new NoSpaceException("Full column"); //check if column has enough space
            }
            j++;
            i++;

        }

        j = 0;
        i -= cards.length; //set back to correct starting line after column check

        while(j < cards.length){

            board[column][i] = cards[j]; //add cards
            j++;
            i++;

        }

        for(int k = 0; k < 5; k++){
            if(board[k][5].getColor().equals(EMPTY)){ //check if PlayerBoard is completely full
                flag = true;
            }
        }
        return !flag;

    }

}
