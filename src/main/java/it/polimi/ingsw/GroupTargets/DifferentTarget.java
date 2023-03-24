package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.*;
import static it.polimi.ingsw.Cards.CardColor.EMPTY;

import java.util.Arrays;
import java.util.HashSet;

public class DifferentTarget extends CommonGoal {
    /**
     * @param cards array of cards to compare
     * @return boolean true if there aren't two (or more) card of the same color in the array
     */
    public boolean different(Card[] cards) {
        Card emptyCard = new Card(EMPTY);
        HashSet<Card> hs = new HashSet<>(Arrays.asList(cards));          //create hash set with same element of the array
        return hs.size() == cards.length && !hs.contains(emptyCard);     //if hs and cards has same lenght => all element are different
    }
}
