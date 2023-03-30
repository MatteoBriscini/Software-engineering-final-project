package it.polimi.ingsw.JsonSupportClasses;

import it.polimi.ingsw.GameMaster;

public class GameMasterConfig {
    private final int[] coupleAndPokersGoals;
    private final int[] oneColorPatternGoals;
    private final int[] RainbowRowsAndColumnsGoals;

    public GameMasterConfig(int[]coupleAndPokersGoals, int[] OneColorPatternGoals, int[]RainbowRowsAndColumnsGoals){
        this.coupleAndPokersGoals = coupleAndPokersGoals;
        this.oneColorPatternGoals = OneColorPatternGoals;
        this.RainbowRowsAndColumnsGoals = RainbowRowsAndColumnsGoals;
    }
    public int[] getCoupleAndPokersGoals() {
        return coupleAndPokersGoals;
    }
    public int[] getOneColorPatternGoals() {
        return oneColorPatternGoals;
    }
    public int[] getRainbowRowsAndColumnsGoals() {
        return RainbowRowsAndColumnsGoals;
    }
}
