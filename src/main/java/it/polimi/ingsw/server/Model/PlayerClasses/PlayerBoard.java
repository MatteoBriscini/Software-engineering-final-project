package it.polimi.ingsw.server.Model.PlayerClasses;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.Exceptions.NoSpaceException;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.JsonSupportClasses.JsonUrl;

import java.io.*;

import static it.polimi.ingsw.Shared.Cards.CardColor.EMPTY;

public class PlayerBoard {


    //Attributes

    private Card[][] board;
    private int x;
    private int y;
    JsonObject playerBoardConfig = new JsonObject();
    private JsonUrl jsonUrl;

    //Constructor

    public PlayerBoard(){
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        board = new Card[x][y];
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                board[i][j] = new Card(EMPTY);
            }
        }
    }

    public PlayerBoard(Card[][] board){
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.board = new Card[x][y];
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                this.board[i][j] = board[i][j];
            }
        }
    }

    //Get Methods

    public Card[][] getBoard() {
        Card[][] card = new Card[this.x][this.y];
        for(int x=0; x<this.x; x++){
            for(int y=0; y<this.y; y++){
                card[x][y] = new Card(board[x][y].getColor());
            }
        }
        return card;
    }

    private void jsonCreate() throws FileNotFoundException{
        Gson gson = new Gson();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("playerBoardConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        JsonObject controller = new Gson().fromJson(bufferedReader, JsonObject.class);
        this.x = controller.get("x").getAsInt();
        this.y = controller.get("y").getAsInt();

    }
    //Methods

    /**
     * Adds cards to the PlayerBoard
     * @param column index of column for the player board
     * @param cards array of Card objects
     * @return true if player board is full, false in all other cases
     * @throws NoSpaceException if the selected column is full or there is not enough space for the selected number of cards
     */
    public boolean addCard (int column, Card[] cards) throws NoSpaceException{

        int i = y-1, j = 0;
        boolean flag = false;

        while(board[column][i].getColor().equals(EMPTY) && i != 0){ //check column for correct line start

            i--;
        }

        if(!board[column][i].getColor().equals(EMPTY)) i++; //correcting for empty column

        while(j < cards.length) {

            if (i >= y) {
                throw new NoSpaceException("Full column"); //check if column has enough space
            }
            j++;
            i++;

        }

        j = 0;
        i -= cards.length; //set back to correct starting line after column check

        while(j < cards.length){

            board[column][i] = cards[j]; //add cards
            j++;
            i++;

        }

        for(int k = 0; k < x; k++){
            if(board[k][y-1].getColor().equals(EMPTY)){ //check if PlayerBoard is completely full
                flag = true;
            }
        }
        return !flag;

    }



}
