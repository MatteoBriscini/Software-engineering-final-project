package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.*;
import static it.polimi.ingsw.Cards.CardColor.EMPTY;

import java.util.Arrays;
import java.util.HashSet;

public class NElementsCheck extends CommonGoal {
    /**
     * @param cards array of cards to compare
     * @param min min of group the method have to fine in the groups
     * @param max max of group the method have to fine in the groups
     * @return boolean true if min and max are respected
     */
    public boolean nColorsCheck  (Card[] cards, int min, int max) {
        HashSet<Card> hs = new HashSet<>(Arrays.asList(cards)); //create hash set with same element of the array
        for (Card c : hs){                                      //verify there are no empty card
            if (c.getColor().equals(EMPTY)) {return false;}
        }
        return (hs.size() >= min && hs.size()<= max);           //return true if there are more element then min and less then max
    }
}
