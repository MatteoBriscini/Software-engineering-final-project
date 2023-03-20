package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.CardColor;

import java.util.Arrays;
import java.util.HashSet;

public class DifferentTarget extends GroupTarget{
    private boolean different(CardColor[] cards) {
        HashSet<CardColor[]> hs = new HashSet<>(Arrays.asList(cards));
        if(hs.size() == cards.length) {return true;}
        else {return false;}
    }
}
