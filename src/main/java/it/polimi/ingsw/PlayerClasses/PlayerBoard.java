package it.polimi.ingsw.PlayerClasses;


import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardColor;

public class PlayerBoard {


    //Attributes

    private static Card[][] board;


    //Constructor

    public PlayerBoard(){
        board = new Card[6][5];
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 5; j++){
                board[i][j] = new Card(CardColor.EMPTY, 0);
            }
        }
    }


    //Methods

    public void addCard(int column, Card[] cards){

        int i = 5, j = 0;

        while(board[i][column] == null){
            i--;
        }

        while(cards[j] != null && j < 3){

            i++;
            board[i][column] = cards[j];
            j++;

        }

    }

}
