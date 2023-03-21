package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardColor;
import it.polimi.ingsw.PlayerClasses.PlayerBoard;
import junit.framework.TestCase;

import static it.polimi.ingsw.Cards.CardColor.*;

public class GT1_3Test extends TestCase {

    private GT1_3 test = new GT1_3(2,6);
    private PlayerBoard playerBoard;
    private Card[] tmp = new Card[3];

    public void testCheck() {
        System.out.println("START TEST \n");

        playerBoard = new PlayerBoard();


        //check initial state
        for (Card[] array : playerBoard.getBoard()){
                    for(Card c: array){
                        assert (c.getColor().equals(EMPTY));
                    }
        }

        //test case 2 (board full blue, have to return false)
        tmp [0] = new Card (BLUE, 0);
        tmp [1] = new Card (BLUE, 0);
        tmp [2] = new Card (BLUE, 0);


        playerBoard.addCard(0, tmp);






        System.out.println("END TEST \n");
    }

}