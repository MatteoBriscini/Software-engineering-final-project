package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.*;
import static it.polimi.ingsw.Cards.CardColor.EMPTY;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

public class EqualTarget extends CommonGoal {
    /**
     * @param cards array of cards to compare
     * @return boolean true if all the array is equal
     */
    public boolean allEqual (Card[] cards) {
        Card emptyCard = new Card(EMPTY);
        HashSet<Card> hs = new HashSet<>(Arrays.asList(cards));          //create hash set with same element of the array
        return hs.size() == 1 && !hs.contains(emptyCard);             //if hs size is equal 1 all element are equals
    }
}
