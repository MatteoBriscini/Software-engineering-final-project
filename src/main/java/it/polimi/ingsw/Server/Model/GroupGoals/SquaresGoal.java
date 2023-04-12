package it.polimi.ingsw.Server.Model.GroupGoals;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Server.SupportClasses.NColorsGroup;
import it.polimi.ingsw.Server.SupportClasses.RecursiveUsed;
import it.polimi.ingsw.Server.SupportClasses.RecursiveUsedSupport;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * this class checks the common goal which requires the existence of two groups each containing 4 tiles of the same type
 * in a 2x2 square (tiles of one square can be different from those of the other square)
 */
public class SquaresGoal extends CommonGoal{

    private final RecursiveUsed recursiveUsed = new RecursiveUsed();
    private boolean[][] alreadyUsed;
    private int validCombo;  //amount of valid squares

    private final NColorsGroup nColor = new NColorsGroup();

    private final String url = "src/main/json/goal/SquaresGoal.json";

    int maxX,maxY,nColorsMin,nColorsMax,nSquares;


    public SquaresGoal(){
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("SquaresGoal: JSON FILE NOT FOUND");
        }
    }

    private void jsonCreate() throws FileNotFoundException {
        String urlConfig = url;
        FileReader fileJsonConfig = new FileReader(urlConfig);

        JsonObject controller = new Gson().fromJson(fileJsonConfig, JsonObject.class);

        this.maxX = controller.get("maxX").getAsInt();
        this.maxY = controller.get("maxY").getAsInt();
        this.nColorsMin = controller.get("nColorsMin").getAsInt();
        this.nColorsMax = controller.get("nColorsMax").getAsInt();
        this.nSquares = controller.get("nSquares").getAsInt();


    }

    /**
     *
     * @param board is a matrix that represents the main board
     * @return true if the goal has been reached, false otherwise
     */
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
