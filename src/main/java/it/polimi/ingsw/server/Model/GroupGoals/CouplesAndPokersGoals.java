package it.polimi.ingsw.server.Model.GroupGoals;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.server.Exceptions.ConstructorException;
import it.polimi.ingsw.server.SupportClasses.NColorsGroup;
import it.polimi.ingsw.server.SupportClasses.RecursiveUsed;
import it.polimi.ingsw.server.SupportClasses.RecursiveUsedSupport;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;

import java.io.*;

public class CouplesAndPokersGoals extends CommonGoal{

    /**
     * parameters
     */
    private boolean[][] alreadyUsed = new boolean[5][6];
    private int validCombo = 0;  //amount of valid row
    private int elementCombo = 0;  //amount of element in a single combo
    private final NColorsGroup equal = new NColorsGroup();
    private final RecursiveUsed recursiveUsed = new RecursiveUsed();

    private int rowSize;
    private int columnSize;
    private JsonArray nCardsArray;
    private JsonArray mGroupsArray;
    private JsonUrl jsonUrl;
    private final int n, mGroups;

    /**
     * constructor of the goals, have different configuration for different goal
     * @param n is number of element in one group
     * @param mGroups mGroups is the number of groups required to reach the goal
     * @throws ConstructorException if n ore mGroups aren't allowed value
     */
    public CouplesAndPokersGoals(int n, int mGroups) throws ConstructorException { //possible value for n (4 o 2) && for mGroups (4 o 6);
        try {
            jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("CouplesAndPokers: JSON FILE NOT FOUND");
            throw new RuntimeException(e);
        }

        for(int i = 0; i<mGroupsArray.size(); i++){
            if(nCardsArray.get(i).getAsInt() == n && mGroupsArray.get(i).getAsInt()==mGroups) {
                this.n = n;
                this.mGroups = mGroups;
                return;
                //value to distinguish different case (t1 and t3)
            }
        }
        throw new ConstructorException("invalid parameter for CouplesAndPokersGoals( costructor (possible value n: 2/4 mGroups 6/4)");
    }

    public void jsonCreate() throws FileNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("playerBoardConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("couplesAndPokersConfig"));
        if(inputStream1 == null) throw new FileNotFoundException();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));

        JsonObject jsonObject = new Gson().fromJson(bufferedReader, JsonObject.class);
        this.rowSize = jsonObject.get("x").getAsInt();
        this.columnSize = jsonObject.get("y").getAsInt();

        jsonObject = new Gson().fromJson(bufferedReader1, JsonObject.class);
        this.nCardsArray = jsonObject.get("nCards").getAsJsonArray();
        this.mGroupsArray = jsonObject.get("mGroups").getAsJsonArray();
    }
    /**
     * actual check of the goal
     * search for a couple of card from the same color
     * if the method find the couple cal a recursive method (in recursiveUsed class) which count the card in the group and mark it
     * @param board player board on which method have to check the goal
     * @return boolean true if the player has reach the goal
     */
    @Override
    public boolean check(Card[][] board){
        int i,j;
        alreadyUsed = new boolean[5][6];
        for (i=0; i<rowSize ; i++){
            for (j=0; j<columnSize; j++){
                if (!alreadyUsed[i][j] &&( i+1<rowSize && (equal.nColorsCheck(new Card[]{board[i][j], board[i+1][j]}, 1, 1)) || j+1<columnSize && (equal.nColorsCheck(new Card[]{board[i][j], board[i][j+1]},1, 1)))) { //search for first pair
                    RecursiveUsedSupport used = recursiveUsed.used(board, i, j, alreadyUsed, 0);     //call method to save just used position
                    alreadyUsed = used.getAlreadyUsed();
                    elementCombo = used.getElementCombo();
                    if (elementCombo >= n) validCombo += 1;
                }
            }
        }
        return validCombo >= mGroups;
    }
}
