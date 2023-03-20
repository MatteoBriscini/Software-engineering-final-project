package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.*;
import static it.polimi.ingsw.Cards.CardColor.EMPTY;

import java.util.Arrays;
import java.util.HashSet;

public class DifferentTarget extends GroupTarget{
    public boolean different(Card[] cards) {
        HashSet<Card> hs = new HashSet<>(Arrays.asList(cards));          //create hash set with same element of the array
        for (Card c : hs){                                              //verify there are no empty card
            if (c.getColor().equals(EMPTY)) {return false;}
        }
        return (hs.size() == cards.length);                             //if hs and cards has same lenght => all element are different
    }
}
