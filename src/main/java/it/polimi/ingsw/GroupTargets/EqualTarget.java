package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.*;

import java.util.Arrays;
import java.util.HashSet;

public class EqualTarget extends GroupTarget{
    public boolean allEqual (Card[] cards) {
        HashSet<Card> hs = new HashSet<>(Arrays.asList(cards));
        if(hs.size() == 1 ) {return true;}
        else {return false;}
    }
}
