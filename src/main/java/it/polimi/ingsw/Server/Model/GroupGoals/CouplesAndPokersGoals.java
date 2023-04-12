package it.polimi.ingsw.Server.Model.GroupGoals;

import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Server.Exceptions.ConstructorException;
import it.polimi.ingsw.Server.SupportClasses.NColorsGroup;
import it.polimi.ingsw.Server.SupportClasses.RecursiveUsed;
import it.polimi.ingsw.Server.SupportClasses.RecursiveUsedSupport;

public class CouplesAndPokersGoals extends CommonGoal{

    /**
     * parameters
     */
    private boolean[][] alreadyUsed = new boolean[5][6];
    private int validCombo = 0;  //amount of valid row
    private int elementCombo = 0;  //amount of element in a single combo
    private final NColorsGroup equal = new NColorsGroup();
    private final RecursiveUsed recursiveUsed = new RecursiveUsed();

    private final int n, mGroups;

    /**
     * constructor of the goals, have different configuration for different goal
     * @param n is number of element in one group
     * @param mGroups mGroups is the number of groups required to reach the goal
     * @throws ConstructorException if n ore mGroups aren't allowed value
     */
    public CouplesAndPokersGoals(int n, int mGroups) throws ConstructorException { //possible value for n (4 o 2) && for mGroups (4 o 6);
        if((n==4 && mGroups==4) || (n==2 && mGroups==6)) {
            this.n = n;
            this.mGroups = mGroups;
            //value to distinguish different case (t1 and t3)
        } else throw new ConstructorException("invalid parameter for CouplesAndPokersGoals( costructor (possible value n: 2/4 mGroups 6/4)");
    }

    /**
     * actual check of the goal
     * search for a couple of card from the same color
     * if the method find the couple cal a recursive method (in recursiveUsed class) which count the card in the group and mark it
     * @param board player board on which method have to check the goal
     * @return boolean true if the player has reach the goal
     */
    public boolean check(Card[][] board){
        int i,j;
        alreadyUsed = new boolean[5][6];
        for (i=0; i<5; i++){
            for (j=0; j<6; j++){
                if (!alreadyUsed[i][j] &&( i+1<5 && (equal.nColorsCheck(new Card[]{board[i][j], board[i+1][j]}, 1, 1)) || j+1<6 && (equal.nColorsCheck(new Card[]{board[i][j], board[i][j+1]},1, 1)))) { //search for first pair
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
