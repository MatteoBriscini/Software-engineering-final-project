package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.CostructorExeception;
import it.polimi.ingsw.SupportClasses.RecursiveUsed;
import it.polimi.ingsw.SupportClasses.RecursiveUsedSupport;

/**
 * this class checks the common goal which requires the existence of two groups each containing 4 tiles of the same type
 * in a 2x2 square (tiles of one square can be different from those of the other square)
 */
public class SquaresGoal extends EqualTarget{

    private final RecursiveUsed recursiveUsed = new RecursiveUsed();
    private boolean[][] alreadyUsed = new boolean[5][6];
    private int validCombo = 0;  //amount of valid squares
    private final int n;

    public SquaresGoal(int n) throws CostructorExeception {
        if(n==4)
            this.n=n;
        else
            throw new CostructorExeception("invalid parameter for SquaresGoals [constructor possible value n: 2]");
    }

    /**
     *
     * @param board
     * @return
     */
    public boolean check(Card[][] board) {
        int x, y;
        for (x = 0; x < 5; x++) {
            for (y = 0; y < 6; y++) {
                if (!alreadyUsed[x][y] && (x + 1 < 5 && (allEqual(new Card[]{board[x][y], board[x + 1][y]})) || y + 1 < 6 && (allEqual(new Card[]{board[x][y], board[x + 1][y], board[x][y + 1], board[x + 1][y + 1]})))) {
                    RecursiveUsedSupport used = recursiveUsed.used(board, x, y, alreadyUsed, 0);     //call method to save just used position
                    alreadyUsed = used.getAlreadyUsed();
                    validCombo += 1;
                    if (validCombo > 1)
                        return true;
                }
            }
        }
        return false;
    }
}
