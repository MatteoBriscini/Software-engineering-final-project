package it.polimi.ingsw.Server.Model.PlayerClasses;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Server.Model.Cards.*;
import it.polimi.ingsw.Server.Exceptions.NoSpaceException;
import it.polimi.ingsw.Server.Model.Cards.Card;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static it.polimi.ingsw.Server.Model.Cards.CardColor.EMPTY;

public class PlayerBoard {


    //Attributes

    private Card[][] board;
    private int x;
    private int y;

    JsonObject playerBoardConfig = new JsonObject();

    private static final String jsonURL = "src/main/json/config/playerBoardConfig.json";


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

    private void jsonCreate() throws FileNotFoundException{

        Gson gson = new Gson();

        String urlConfig = jsonURL;
        FileReader fileJsonConfig = new FileReader(urlConfig);

        JsonObject controller = new Gson().fromJson(fileJsonConfig, JsonObject.class);
        this.x = controller.get("x").getAsInt();
        this.y = controller.get("y").getAsInt();

    }

}
