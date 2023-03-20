package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.*;

import java.util.Arrays;
import java.util.HashSet;

public class DifferentTarget extends GroupTarget{
    public boolean different(Card[] cards) {
        HashSet<Card> hs = new HashSet<>(Arrays.asList(cards));
        if(hs.size() == cards.length) {return true;}
        else {return false;}
    }
}
