package it.polimi.ingsw.Server.Model.GroupGoals;

import it.polimi.ingsw.Server.Model.Cards.Card;
import it.polimi.ingsw.Server.Model.PlayerClasses.Player;

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
