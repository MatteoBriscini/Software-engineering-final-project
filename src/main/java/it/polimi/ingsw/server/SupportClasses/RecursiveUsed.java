package it.polimi.ingsw.server.SupportClasses;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;

import java.io.*;

/**
 * support class to avoid duplicate code line
 */
public class RecursiveUsed {
    /**
     * parameters
     */
    private final NColorsGroup equal = new NColorsGroup();
    private boolean[][] alreadyUsed;
    private int elementCombo;
    private JsonUrl jsonUrl = new JsonUrl();
    private int rowSize;
    private int columnSize;

    /**
     * this method just call the recursiveUsed method and the constructor for RecursiveUsedSupport
     * @param board player board
     * @param i x value one the player board
     * @param j y value one the player board
     * @param alreadyUsed boolean matrix to take track of just used card
     * @param elementCombo total amount of card of the same color in a single shape
     * @return RecursiveUsedSupport to make possible to return 2 parameters
     */
    public RecursiveUsedSupport used(Card[][] board, int i, int j, boolean[][] alreadyUsed, int elementCombo){
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("RecursiveUsedSupport: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }
        this.elementCombo = elementCombo;
        this.alreadyUsed = alreadyUsed;
        this.recursiveUsed(board, i, j);
        return new RecursiveUsedSupport(this.alreadyUsed, this.elementCombo);
    }

    public void jsonCreate() throws FileNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("playerBoardConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        JsonObject jsonObject = new Gson().fromJson(bufferedReader, JsonObject.class);
        this.rowSize = jsonObject.get("x").getAsInt();
        this.columnSize = jsonObject.get("y").getAsInt();
    }

    /**
     * recursive method mark as already used all the card of the same color of the combo which are adjacent with the combo
     * @param board player board
     * @param i x value one the player board
     * @param j y value one the player board
     */
    private void recursiveUsed (Card[][] board, int i, int j){
        alreadyUsed[i][j] = true;
        this.elementCombo += 1;
        if (i>0 && i<rowSize && !alreadyUsed[i-1][j] && equal.nColorsCheck(new Card[]{board[i][j], board[i-1][j]},1,1))
            this.recursiveUsed (board, i-1, j);
        if (j>0 && j<columnSize && !alreadyUsed[i][j-1] && equal.nColorsCheck(new Card[]{board[i][j], board[i][j-1]},1 ,1))
            this.recursiveUsed (board, i, j-1);
        if (i+1<rowSize && !alreadyUsed[i+1][j] &&  equal.nColorsCheck(new Card[]{board[i][j], board[i+1][j]}, 1,1))
            this.recursiveUsed (board, i+1, j);
        if (j+1<columnSize && !alreadyUsed[i][j+1] && equal.nColorsCheck(new Card[]{board[i][j], board[i][j+1]},1,1))
            this.recursiveUsed (board, i, j+1);
    }
}

