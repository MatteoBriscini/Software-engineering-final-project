package it.polimi.ingsw.Server.Model.GroupGoals;

import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Server.SupportClasses.NColorsGroup;
import it.polimi.ingsw.Server.SupportClasses.RecursiveUsed;
import it.polimi.ingsw.Server.SupportClasses.RecursiveUsedSupport;

/**
 * this class checks the common goal which requires the existence of two groups each containing 4 tiles of the same type
 * in a 2x2 square (tiles of one square can be different from those of the other square)
 */
public class SquaresGoal extends CommonGoal{

    private final RecursiveUsed recursiveUsed = new RecursiveUsed();
    private boolean[][] alreadyUsed;
    private int validCombo;  //amount of valid squares

    private final NColorsGroup nColor = new NColorsGroup();
    public SquaresGoal() {}

    /**
     *
     * @param board is a matrix that represents the main board
     * @return true if the goal has been reached, false otherwise
     */
    public boolean check(Card[][] board) {
        int x, y;
        validCombo=0;
        alreadyUsed = new boolean[5][6];

        for (x = 0; x < 4; x++) {
            for (y = 0; y < 5; y++) {
                if (!alreadyUsed[x][y] && nColor.nColorsCheck(new Card[]{board[x][y], board[x+1][y+1], board[x+1][y], board[x][y+1]}, 1, 1)) {
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
