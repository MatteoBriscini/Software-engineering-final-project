package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.CardColor;

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



    //public abstract void check();
}
