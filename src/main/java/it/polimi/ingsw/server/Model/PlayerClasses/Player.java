package it.polimi.ingsw.server.Model.PlayerClasses;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.server.Exceptions.NoSpaceException;
import it.polimi.ingsw.server.SupportClasses.NColorsGroup;
import it.polimi.ingsw.server.SupportClasses.RecursiveUsed;
import it.polimi.ingsw.server.SupportClasses.RecursiveUsedSupport;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;

import java.io.*;

public class Player {


    //Attributes

    private final String playerID;
    private int pointSum;
    //private int pointArray;
    private PlayerBoard board;
    private PlayerTarget personalTarget;
    private boolean[][] alreadyUsed1= new boolean[5][6];
    private int elementCombo;
    private final NColorsGroup equal = new NColorsGroup();
    private final RecursiveUsed recursiveUsed = new RecursiveUsed();
    //json
    private int rowSize;
    private int columnSize;
    private int maxElement;
    private int minElement;
    private JsonArray pt = new JsonArray();

    //Constructor

    public Player(String playerID){

        this.playerID = playerID;
        pointSum = 0;
        board = new PlayerBoard();
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("Player: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }
    }

    public boolean addCard(int column, Card[] cards) throws NoSpaceException {
        return board.addCard(column, cards);
    }

    private void jsonCreate() throws FileNotFoundException {  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("playerBoardConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("checkSpotConfig"));
        if(inputStream1 == null) throw new FileNotFoundException();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));

        JsonObject jsonObject = new Gson().fromJson(bufferedReader , JsonObject.class);
        this.rowSize = jsonObject.get("x").getAsInt();
        this.columnSize = jsonObject.get("y").getAsInt();
        jsonObject = new Gson().fromJson( bufferedReader1, JsonObject.class);
        this.pt =  jsonObject.get("pt").getAsJsonArray();
        this.maxElement =  jsonObject.get("maxElement").getAsInt();
        this.minElement =  jsonObject.get("minElement").getAsInt();
    }

    //Set Methods

    public void setBoard(PlayerBoard board) {
        this.board = board;
    }

    /**
     * set the board from a matrix of Card
     * @param board matrix of Card
     */
    public void setBoardNonRandomBoard(Card[][] board) {
        this.board = new PlayerBoard(board);
    }

    /**
     * Reads the JSON file and assigns the PlayerTarget to the Player
     * @param targetNumber, indicates which of the twelve PersonalTargets is to be assigned to the player
     * @targets is an array of objects PlayerTarget
     * @throws FileNotFoundException
     */
    public void setPlayerTarget(int targetNumber) throws FileNotFoundException {  //exception file not found
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("playerTarget"));

        if(inputStream == null) {
            throw new FileNotFoundException();
        }


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //JsonArray jsonArray = new Gson().fromJson(bufferedReader , JsonArray.class);


        PlayerTarget[] targets = new Gson().fromJson(bufferedReader, PlayerTarget[].class);       //Call constructor on PlayerTarget array by passing the json file attributes

        this.personalTarget = targets[targetNumber];
    }


    //Get Methods

    public String getPlayerID() {
        return playerID;
    }

    public int getPointSum() {
        return this.pointSum;
    }

    public Card[][] getBoard(){
        return this.board.getBoard();
    }

    public PlayerTarget getPersonalTarget() {
        return new PlayerTarget(personalTarget);
    }

    //Other Methods

    public void updatePointSum(int pointSum) {

        this.pointSum += pointSum;
    }

    public boolean checkBoardSpace(int column, int cards){
        return board.checkSpace(column,cards);
    }

    public void checkPlayerTarget(){
        int result = personalTarget.checkTarget(board);
        updatePointSum(result);
    }

    //get points for spots
    public void checkSpots() {
        Card[][] tmpBoard = board.getBoard();
        int i, j;
        alreadyUsed1 = new boolean[rowSize][columnSize];
        for (i = 0; i < rowSize; i++) {
            for (j = 0; j < columnSize; j++) {
                if (!alreadyUsed1[i][j] && (i + 1 < rowSize && (equal.nColorsCheck(new Card[]{tmpBoard[i][j], tmpBoard[i + 1][j]},1,1)) || (j + 1 < columnSize && equal.nColorsCheck(new Card[]{tmpBoard[i][j], tmpBoard[i][j + 1]},1,1)))) {
                    RecursiveUsedSupport used = recursiveUsed.used(tmpBoard, i, j, alreadyUsed1, 0);
                    alreadyUsed1 = used.getAlreadyUsed();
                    elementCombo = used.getElementCombo();

                    if (elementCombo >= maxElement) {        //assign points for exception spots bigger than maxElement cards
                        this.elementCombo = maxElement;
                    }
                    //assign points for the spots
                    if(elementCombo >= minElement) {
                        this.updatePointSum(pt.get(elementCombo - minElement).getAsInt());
                    }
                    elementCombo = 0;

                }

            }
        }

    }


}
