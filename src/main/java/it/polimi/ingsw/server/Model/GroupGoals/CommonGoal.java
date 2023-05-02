package it.polimi.ingsw.server.Model.GroupGoals;

import it.polimi.ingsw.shared.Cards.Card;

import java.util.ArrayList;

public abstract class CommonGoal {
    private ArrayList<String> alreadyScored = new ArrayList<>();

    public ArrayList<String> getAlreadyScored(){
        return alreadyScored;
    }

    public void setAlreadyScored(ArrayList<String> alreadyScored) {
        this.alreadyScored = alreadyScored;
    }

    public abstract boolean check(Card[][] board);

    //public abstract void check();
}
