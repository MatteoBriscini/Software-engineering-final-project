package it.polimi.ingsw.Shared.JsonSupportClasses;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Shared.Cards.Card;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import static it.polimi.ingsw.Shared.Cards.CardColor.EMPTY;

public class Position {
    private final int x;
    private final int y;
    private int rowSize;
    private int columnSize;
    private JsonUrl jsonUrl;
    public Position(int x, int y){
        this.x = x;
        this.y = y;
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("Position: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }
    }

    public void jsonCreate() throws FileNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("mainBoardConfig"));
        if (inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader, JsonObject.class);
        this.rowSize = jsonObject.get("rows").getAsInt();
        this.columnSize = jsonObject.get("columns").getAsInt();
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
        if (x+1<rowSize) neighbors.add(new Position(x+1, y));
        if (y+1<columnSize) neighbors.add(new Position(x, y+1));
        return neighbors.toArray(new Position[0]);
    }

    /**
     * @param board the actual mainBoard
     * @return boolean, true if one of the adjacent of this Position are EMPTY
     * if you call this method on an empty position it will be return ture
     */
    public boolean pickable(Card[][] board){
        Card emptyCard = new Card(EMPTY);
        HashSet<Card> neighborsCard = new HashSet<>();
        Position[] neighbors = getNeighbors();


        for (Position p: neighbors){
            neighborsCard.add(board[p.getX()][p.getY()]);
        }
        if(this.getX()==0 || this.getY()==0 || this.getX()==rowSize-1 || this.getY()==columnSize-1)neighborsCard.add(emptyCard);
        return neighborsCard.contains(emptyCard) && !board[this.getX()][this.getY()].getColor().equals(EMPTY);
    }
}