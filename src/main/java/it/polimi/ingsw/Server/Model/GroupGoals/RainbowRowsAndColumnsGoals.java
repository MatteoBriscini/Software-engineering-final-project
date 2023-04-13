package it.polimi.ingsw.Server.Model.GroupGoals;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Server.Exceptions.ConstructorException;
import it.polimi.ingsw.Server.SupportClasses.NColorsGroup;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * this class checks the following common goals:
 * three columns each formed by 6 tiles of maximum 3 different types,
 * four rows each formed by 5 tiles of maximum 3 different types,
 * two columns each formed by 6 different types of tiles,
 * two rows each formed by 5 different types of tiles,
 */
public class RainbowRowsAndColumnsGoals extends CommonGoal{
    private int n,min,max,tot;
    private final NColorsGroup nColor = new NColorsGroup();

    private List<Card> cardsRow;
    private List<Card> cardsColumn;

    private final String url = "src/main/json/goal/RainbowRowsAndColumnsGoal.json";
    private final String urlBoard = "src/main/json/config/playerBoardConfig.json";

    private int rows,columns,minRows,minColumns,maxRows,maxColumns,totRows,totColumns,totRainbowRows,totRainbowColumns;

    /**
     * @param n is the number of Cards required from the goal
     * @param min is the minimum number of different colours required from the goal
     * @param max is the maximum number of different colours required from the goal
     * @param tot is the number of rows or columns that must fulfill the requirement of the number of colors
     */
    public RainbowRowsAndColumnsGoals(int n, int min, int max, int tot) throws ConstructorException {
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("SquaresGoal: JSON FILE NOT FOUND");
        }
        if((n==rows && ((min==minRows && max==maxRows && tot==totRows) || (min==columns && max==columns && tot==totRainbowColumns))) || (n==columns && ((min==minColumns && max==maxColumns && tot==totColumns) || (min==rows && max==rows && tot==totRainbowRows))) ) {
            this.n = n;
            this.min = min;
            this.max = max;
            this.tot = tot;
        }
        else throw new ConstructorException("invalid parameter for RainbowRowsAndColumnsGoals constructor");
    }


    private void jsonCreate() throws FileNotFoundException {
        String urlConfig = url;
        FileReader fileJsonConfig = new FileReader(urlConfig);

        JsonObject rainbow = new Gson().fromJson(fileJsonConfig, JsonObject.class);

        String urlConfigBoard = urlBoard;
        FileReader fileJsonConfigBoard = new FileReader(urlConfigBoard);

        JsonObject rainbowBoard = new Gson().fromJson(fileJsonConfigBoard, JsonObject.class);

        this.rows = rainbowBoard.get("y").getAsInt();
        this.columns = rainbowBoard.get("x").getAsInt();
        this.minRows = rainbow.get("minRows").getAsInt();
        this.minColumns = rainbow.get("minColumns").getAsInt();
        this.maxRows = rainbow.get("maxRows").getAsInt();
        this.maxColumns = rainbow.get("maxColumns").getAsInt();
        this.totRows = rainbow.get("totRows").getAsInt();
        this.totColumns = rainbow.get("totColumns").getAsInt();
        this.totRainbowRows = rainbow.get("totRainbowRows").getAsInt();
        this.totRainbowColumns = rainbow.get("totRainbowColumns").getAsInt();


    }

    /**
     * @param board is a matrix that represents the main board
     * @return true if the goal has been reached, false otherwise
     */
    public boolean check(Card board[][]){
        int tmptot = tot;

        /** the rows are made by 5 Cards, while the columns is made by 6 Cards.
         ** depending on the value of n, this loop checks how many rows or columns fulfill the requirements of the goal
         ** (if n==6 the check is made on the rows, if n==5 it is made on the columns
         * */
        for(int i=0;i<n;i++) {
            if(i<rows) {
                cardsRow = new ArrayList<>();
                for (int j = 0; j < columns; j++)
                    cardsRow.add(board[j][i]);
            }

            if(i<columns) {
                cardsColumn = new ArrayList<>();
                for (int j = 0; j < rows; j++)
                    cardsColumn.add(board[i][j]);
            }

            if((n==rows && (nColor.nColorsCheck(cardsRow.toArray(new Card[0]), min, max))) || (n==columns && (nColor.nColorsCheck(cardsColumn.toArray(new Card[0]), min, max)))) {
                {
                    tmptot--;
                }
                if(tmptot==0) {
                    return true;
                }
            }
        }
        return false;
    }
}
