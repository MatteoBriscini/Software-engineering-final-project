package it.polimi.ingsw.JsonSupportClasses;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardColor;

import java.util.ArrayList;
import java.util.HashSet;

import static it.polimi.ingsw.Cards.CardColor.EMPTY;

public class Position {
    private final int x;
    private final int y;


    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * @return an array with all Position obj adjacent
     */
    public Position[] getNeighbors(){
        ArrayList<Position> neighbors = new ArrayList<>();
        if (x-1 >= 0) neighbors.add(new Position(x-1, y));
        if (y-1 >= 0) neighbors.add(new Position(x, y-1));
        if (x+1<5) neighbors.add(new Position(x+1, y));
        if (y+1<6) neighbors.add(new Position(x, y+1));
        return neighbors.toArray(new Position[0]);
    }

    /**
     * @param board the actual mainBoard
     * @return boolean, true if one of the adjacent of this Position are EMPTY
     * if you call this method on an empty position it will be return false
     */
    public boolean pickable(Card[][] board){
        Card emptyCard = new Card(EMPTY);
        HashSet<Card> neighborsCard = new HashSet<>();
        Position[] neighbors = getNeighbors();

        for (Position p: neighbors){
            neighborsCard.add(board[p.getX()][p.getY()]);
        }
        System.out.println(neighbors.length);
        return neighborsCard.contains(emptyCard) && !board[this.getX()][this.getY()].getColor().equals(EMPTY);
    }
}
