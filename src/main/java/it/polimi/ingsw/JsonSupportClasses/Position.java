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

    public Position[] getNeighbors(){
        ArrayList<Position> neighbors = new ArrayList<>();
        if (x-1 >= 0) neighbors.add(new Position(x-1, y));
        if (y-1 >= 0) neighbors.add(new Position(x, y-1));
        if (x+1<5) neighbors.add(new Position(x+1, y));
        if (y+1<6) neighbors.add(new Position(x, y+1));
        return neighbors.toArray(new Position[0]);
    }
    
    public boolean pickable(Card[][] board){
        Card emptyCard = new Card(EMPTY);
        HashSet<Card> neighborsCard = new HashSet<>();
        Position[] neighbors = getNeighbors();
        for (Position p: neighbors){
            neighborsCard.add(board[p.getX()][p.getY()]);
        }
        return neighborsCard.contains(emptyCard);
    }
}
