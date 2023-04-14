package it.polimi.ingsw.Shared.JsonSupportClasses;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonUrl {
    public static String getUrl(String fName) throws FileNotFoundException {
        switch (fName){
            case "controllerConfig": return "json/config/controllerConfig.json";
            case "gameMasterConfig": return "json/config/gameMasterConfig.json";
            case "gameConfig": return "json/config/gameConfig.json";
            case "couplesAndPokersConfig": return "json/goal/CouplesAndPokersGoalsConfig.json";
            case "playerBoardConfig": return "json/config/playerBoardConfig.json";
            case "mainBoardConfig": return "json/config/MainBoardConfig.json";
            case "mainBoard3Players": return "json/testJson/mainBoard3Players.json";
            case "quiteVoidMainBoard": return "json/testJson/quiteVoidMainBoard.json";
            case "playerTarget": return "json/PlayerTarget.json";
            case "cornersGoal": return "json/goal/CornersGoal.json";
            case "diagonAlleyGoal": return "json/goal/DiagonAlleyGoal.json";
            case "crossGoal": return "json/goal/CrossGoal.json";
            case "eightEqualsGoal": return "json/goal/EightEqualsGoal.json";
            case "rainbowRowsAndColumnsGoal": return "json/goal/RainbowRowsAndColumnsGoal.json";
            case "squaresGoal": return "json/goal/SquaresGoal.json";
            case "playerGoalConfig": return  "json/config/PlayerGoalConfig.json";
            case "checkSpotConfig": return  "json/config/checkSpotsConfig.json";
            default: return "invalid";
        }
    }
}
