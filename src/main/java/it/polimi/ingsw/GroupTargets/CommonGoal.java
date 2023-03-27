package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.PlayerClasses.Player;

import java.util.ArrayList;

public abstract class CommonGoal {
    private ArrayList<Player> alreadyScored;

    public ArrayList<Player> getAlreadyScored() {
        return alreadyScored;
    }

    public void setAlreadyScored(ArrayList<Player> alreadyScored) {
        this.alreadyScored = alreadyScored;
    }

    public abstract boolean check(Card[][] board);

    //public abstract void check();
}
