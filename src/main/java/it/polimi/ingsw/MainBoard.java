package it.polimi.ingsw;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.JsonSupportClasses.Position;

import java.security.PrivilegedAction;

import static it.polimi.ingsw.Cards.CardColor.*;

public class MainBoard {

    private int cards[];
    private Card board[][];

    public MainBoard(){
        cards= new int[6];
        for( int a : cards)
            cards[a]=22;
        this.board = new Card[9][9];
    }

    //input array pos accettabili
    public void fillBoard(Position[] positions){
        int col;
        boolean reDo;
        for( Position a : positions){
            if(!board[a.getX()][a.getY()].getColor().equals(EMPTY)){
                //remove card and update counter
            }
            do{
                reDo=false;
                col = (int)Math.random()*5;
                if(cards[col]>0) {
                    switch (col) {
                        case 0: {
                            board[a.getX()][a.getY()] = new Card(BLUE);
                            break;
                        }
                        case 1: {
                            board[a.getX()][a.getY()] = new Card(GREEN);
                            break;
                        }
                        case 2: {
                            board[a.getX()][a.getY()] = new Card(LIGHTBLUE);
                            break;
                        }
                        case 3: {
                            board[a.getX()][a.getY()] = new Card(PINK);
                            break;
                        }
                        case 4: {
                            board[a.getX()][a.getY()] = new Card(WHITE);
                            break;
                        }
                        case 5: {
                            board[a.getX()][a.getY()] = new Card(YELLOW);
                            break;
                        }
                    }
                    cards[col]--;
                }
                else reDo=true;
            }while (reDo);
        }

    }

    public void removeCard(){};
}
