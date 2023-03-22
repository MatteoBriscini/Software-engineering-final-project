package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.JsonSupportClasses.Position;

import java.io.FileNotFoundException;
import com.google.gson.Gson;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class GT2_7_11 extends EqualTarget{

    private String url;
    private Position[][] p;

    ArrayList<Card> cards = new ArrayList<>();

    GT2_7_11(int n) { //n is the number of the goal (possible value 2,7,11)
        switch (n) {
            case 2:
                this.url = "src/main/json/goal/CrossGoal.json";
                break;
            case 7:
                this.url = "src/main/json/goal/DiagonAlleyGoal.json";
                break;
            default:
                System.out.println("INVALID CONSTRUCTOR CALLED");
                return;
        }
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("commonGoal: JSON FILE NOT FOUND");
        }
    }

    private void jsonCreate() throws FileNotFoundException{  //download json data
        assert url != null;
        FileReader fileJson = new FileReader(url);
        Gson gson = new Gson();
        p = gson.fromJson(fileJson, Position[][].class);
    }
    public boolean check(Card[][] board){
        //the json file has an array with the position we have to verify, in this two for we create an array list with the card in "interesting"position
        for (Position[] array : p){
            for (Position pos : array){
                cards.add(board[pos.getX()][pos.getY()]);
            }
            Card[] cardArray = cards.toArray(new Card[0]); //copy the element of the array list in array
            if (this.allEqual(cardArray))return true;   //call all equall with the array just created
        }
        return false;
    }
}
