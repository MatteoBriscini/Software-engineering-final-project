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
                board[i][j] = new Card(EMPTY, 0);
            }
        }
    }

    //Get Methods

    public static Card[][] getBoard() {
        return board;
    }


    //Methods

    public void addCard(int column, Card[] cards){

        int i = 5, j = 0;

        while(board[i][column].getColor().equals(EMPTY)){
            i--;
        }

        while(cards[j] != null && j < 3){

            i++;
            board[i][column] = cards[j];
            j++;

        }

    }

}
