package it.polimi.ingsw.GroupTargets;

import java.util.Arrays;
import java.util.HashSet;

public abstract class GroupTarget {
    private int nPlayer;

    public int getnPlayer() {
        return nPlayer;
    }

    public void setnPlayer(int nPlayer) {
        this.nPlayer = nPlayer;
    }

    private boolean allEqual (Card[] cards) {
        HashSet<Card[]> hs = new HashSet<>(Arrays.asList(cards));
        if(hs.size() == 1) {return true;}
        else {return false;}
    }
    private boolean different(Card[] cards) {
        HashSet<Card[]> hs = new HashSet<>(Arrays.asList(cards));
        if(hs.size() == cards.length) {return true;}
        else {return false;}
    }
    //public abstract void check();
}
