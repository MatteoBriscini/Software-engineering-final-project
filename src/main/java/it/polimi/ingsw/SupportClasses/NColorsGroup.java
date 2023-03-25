package it.polimi.ingsw.SupportClasses;

import it.polimi.ingsw.Cards.Card;

import java.util.Arrays;
import java.util.HashSet;

import static it.polimi.ingsw.Cards.CardColor.EMPTY;

public class NColorsGroup {
    public boolean nColorsCheck(Card[] cards, int min, int max) {
        Card emptyCard = new Card(EMPTY);
        HashSet<Card> hs = new HashSet<>(Arrays.asList(cards));                           //create hash set with same element of the array
        return hs.size() >= min && hs.size()<= max  && !hs.contains(emptyCard);           //return true if there are more element than min and less than max
    }
}
