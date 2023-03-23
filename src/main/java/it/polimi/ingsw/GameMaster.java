package it.polimi.ingsw;

import it.polimi.ingsw.PlayerClasses.*;
import it.polimi.ingsw.GroupTargets.*;

import com.google.gson.Gson;    //import for json
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class GameMaster {
    private ArrayList<Player> players;
    private CommonGoal[] commonGoals = new CommonGoal[2];
    private MainBoard mainBoard;

    public ArrayList<Player> getPlayerArray(){
        return players;
    }


}
