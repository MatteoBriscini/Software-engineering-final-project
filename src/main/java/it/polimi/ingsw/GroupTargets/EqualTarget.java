package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.CardColor;

import java.util.Arrays;
import java.util.HashSet;

public class EqualTarget extends GroupTarget{
    private boolean allEqual (CardColor[] cards) {
        HashSet<CardColor[]> hs = new HashSet<>(Arrays.asList(cards));
        if(hs.size() == 1) {return true;}
        else {return false;}
    }
}
