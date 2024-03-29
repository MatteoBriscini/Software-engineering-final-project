package it.polimi.ingsw.server.Model.GroupGoals;

import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.server.Exceptions.ConstructorException;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.Position;

import java.io.*;

import com.google.gson.Gson;
import it.polimi.ingsw.server.SupportClasses.NColorsGroup;

import java.util.ArrayList;


public class
OneColorPatternGoals extends CommonGoal{

    /**
     * parameters
     */
    private final String url;
    private JsonUrl jsonUrl;
    private Position[][] p;

    private final NColorsGroup equal = new NColorsGroup();
    ArrayList<Card> cards = new ArrayList<>();

    /**
     *  constructor of the goals, have different configuration for different goal
     * @param n is the number of the goal on italian instruction
     * @throws ConstructorException  if n ore mGroups aren't allowed value
     */
    public OneColorPatternGoals(int n) throws ConstructorException { //n is the number of the goal (possible value 2,7,11)
        switch (n) {
            case 2:
                this.url = "cornersGoal";
                break;
            case 7:
                this.url = "diagonAlleyGoal";
                break;
            case 11:
                this.url = "crossGoal";
                break;
            default:
                throw new ConstructorException("invalid parameter for OneColorPatternGoals constructor (possible value 2,7,11)");
        }
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("OneColorPatternGoals: JSON FILE NOT FOUND");
        }
    }

    /**
     * download data from json
     * @throws FileNotFoundException if file not found
     */
    private void jsonCreate() throws FileNotFoundException{  //download json data
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl(url));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();
        p = gson.fromJson(bufferedReader, Position[][].class);
    }

    /**
     * actual check of the goal
     * in the json file are saved some position on the board in a matrix
     * this method create an array to pas to allEqual with the cards color in the position in the json file
     * @param board player board on which method have to check the goal
     * @return boolean true if the player has reach the goal
     */
    @Override
    public boolean check(Card[][] board){
        //the json file has an array with the position we have to verify, in this two for we create an array list with the card in "interesting"position
        for (Position[] array : p){
            cards.clear();
            for (Position pos : array){
                cards.add(board[pos.getX()][pos.getY()]);
            }
            Card[] cardArray = cards.toArray(new Card[0]); //copy the element of the array list in array
            if (equal.nColorsCheck(cardArray, 1, 1))return true;   //call all equal with the array just created
        }
        return false;
    }
}
