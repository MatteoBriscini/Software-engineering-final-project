package it.polimi.ingsw.shared.JsonSupportClasses;

import java.io.FileNotFoundException;

public class JsonUrl {
    public static String getUrl(String fName) throws FileNotFoundException {
        switch (fName){
            case "controllerConfig": return "controllerConfig.json";
            case "gameMasterConfig": return "gameMasterConfig.json";
            case "gameConfig": return "gameConfig.json";
            case "couplesAndPokersConfig": return "CouplesAndPokersGoalsConfig.json";
            case "playerBoardConfig": return "playerBoardConfig.json";
            //case "mainBoardConfigwtf": return "MainBoardConfigwtf.json";
            case "mainBoardConfig": return "MainBoardConfig.json";
            case "mainBoard3Players": return "mainBoard3Players.json";
            case "quiteVoidMainBoard": return "quiteVoidMainBoard.json";
            case "playerTarget": return "PlayerTarget.json";
            case "cornersGoal": return "CornersGoal.json";
            case "diagonAlleyGoal": return "DiagonAlleyGoal.json";
            case "crossGoal": return "CrossGoal.json";
            case "eightEqualsGoal": return "EightEqualsGoal.json";
            case "rainbowRowsAndColumnsGoal": return "RainbowRowsAndColumnsGoal.json";
            case "squaresGoal": return "SquaresGoal.json";
            case "playerGoalConfig": return  "PlayerGoalConfig.json";
            case "checkSpotConfig": return  "checkSpotsConfig.json";
            case "personalBoard1": return "personalBoard1.json";
            case "personalBoard2": return "personalBoard2.json";
            case "personalBoard3": return "personalBoard3.json";
            case "netConfig": return "netConfig.json";
            default: return "invalid";
        }
    }
}
