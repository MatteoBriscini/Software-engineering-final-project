package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.*;
import static it.polimi.ingsw.Cards.CardColor.EMPTY;

import java.util.Arrays;
import java.util.HashSet;

public class EqualTarget extends CommonGoal {
    /**
     * @param cards array of cards to compare
     * @return boolean true if all the array is equal
     */
    public boolean allEqual (Card[] cards) {
        HashSet<Card> hs = new HashSet<>(Arrays.asList(cards));          //create hash set with same element of the array
        for (Card c : hs){                                              //verify there are no empty card
            if (c.getColor().equals(EMPTY)) {return false;}
        }
        return hs.size() == 1 ;                                         //if hs size is equal 1 all element are equals
    }
}
