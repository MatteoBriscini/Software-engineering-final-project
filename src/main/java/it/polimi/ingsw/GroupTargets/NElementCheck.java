package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.*;
import static it.polimi.ingsw.Cards.CardColor.EMPTY;

import java.util.Arrays;
import java.util.HashSet;

public class NElementCheck extends GroupTarget{
    public boolean nColorsCheck  (Card[] cards, int min, int max) {
        HashSet<Card> hs = new HashSet<>(Arrays.asList(cards));
        for (Card c : hs){
            if (c.getColor().equals(EMPTY)) {return false;}
        }
        return (hs.size() >= min && hs.size()<= max);
    }
}
