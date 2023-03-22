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

        while(cards[j] != null && j < 3){

            i++;
            if(i > 5){ throw new NoSpaceException("Full column"); }
            board[column][i] = cards[j];
            j++;

        }

        return false;

    }

}
