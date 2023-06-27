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
    /**
     *
     * @param board is a matrix that represents the main board
     * @return true if the goal has been reached, false otherwise
     */
    public abstract boolean check(Card[][] board);

}
