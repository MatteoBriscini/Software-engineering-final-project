package it.polimi.ingsw.server.Model.GroupGoals;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.server.SupportClasses.NColorsGroup;
import it.polimi.ingsw.server.SupportClasses.RecursiveUsed;
import it.polimi.ingsw.server.SupportClasses.RecursiveUsedSupport;
import it.polimi.ingsw.Shared.JsonSupportClasses.JsonUrl;

import java.io.*;

/**
 * this class checks the common goal which requires the existence of two groups each containing 4 tiles of the same type
 * in a 2x2 square (tiles of one square can be different from those of the other square)
 */
public class SquaresGoal extends CommonGoal{

    private final RecursiveUsed recursiveUsed = new RecursiveUsed();
    private boolean[][] alreadyUsed;
    private int validCombo;  //amount of valid squares
    private final NColorsGroup nColor = new NColorsGroup();
    private JsonUrl jsonUrl;
    int maxX,maxY,nColorsMin,nColorsMax,nSquares;


    public SquaresGoal(){
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("SquaresGoal: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }
    }

    private void jsonCreate() throws FileNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("squaresGoal"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject square = new Gson().fromJson(bufferedReader , JsonObject.class);

        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("playerBoardConfig"));
        if(inputStream1 == null) throw new FileNotFoundException();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
        JsonObject squareBoard = new Gson().fromJson(bufferedReader1, JsonObject.class);

        this.maxX = squareBoard.get("x").getAsInt();
        this.maxY = squareBoard.get("y").getAsInt();
        this.nColorsMin = square.get("nColorsMin").getAsInt();
        this.nColorsMax = square.get("nColorsMax").getAsInt();
        this.nSquares = square.get("nSquares").getAsInt();


    }

    /**
     *
     * @param board is a matrix that represents the main board
     * @return true if the goal has been reached, false otherwise
     */
    @Override
    public boolean check(Card[][] board) {
        int x, y;
        validCombo=0;
        alreadyUsed = new boolean[maxX][maxY];

        for (x = 0; x < maxX-1; x++) {
            for (y = 0; y < maxY-1; y++) {
                if (!alreadyUsed[x][y] && nColor.nColorsCheck(new Card[]{board[x][y], board[x+1][y+1], board[x+1][y], board[x][y+1]}, nColorsMin, nColorsMax)) {
                    RecursiveUsedSupport used = recursiveUsed.used(board, x, y, alreadyUsed, 0);     //call method to save just used position
                    alreadyUsed = used.getAlreadyUsed();
                    validCombo += 1;
                    if (validCombo >= nSquares)
                        return true;
                }
            }
        }
        return false;
    }
}
