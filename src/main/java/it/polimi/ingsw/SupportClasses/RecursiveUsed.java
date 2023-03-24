package it.polimi.ingsw.SupportClasses;

import it.polimi.ingsw.Cards.Card;

/**
 * support class to avoid duplicate code line
 */
public class RecursiveUsed {
    /**
     * parameters
     */
    private final NColorGroup equal = new NColorGroup();
    private boolean[][] alreadyUsed;

    private int elementCombo;

    /**
     * this method just call the recursiveUsed method and the constructor for RecursiveUsedSupport
     * @param board player board
     * @param i x value one the player board
     * @param j y value one the player board
     * @param alreadyUsed boolean matrix to take track of just used card
     * @param elementCombo total amount of card of the same color in a single shape
     * @return RecursiveUsedSupport to make possible to return 2 parameters
     */
    public RecursiveUsedSupport used(Card[][] board, int i, int j, boolean[][] alreadyUsed, int elementCombo){
        this.elementCombo = elementCombo;
        this.alreadyUsed = alreadyUsed;
        this.recursiveUsed(board, i, j);
        return new RecursiveUsedSupport(this.alreadyUsed, this.elementCombo);
    }

    /**
     * recursive method mark as already used all the card of the same color of the combo which are adjacent with the combo
     * @param board player board
     * @param i x value one the player board
     * @param j y value one the player board
     */
    private void recursiveUsed (Card[][] board, int i, int j){
        alreadyUsed[i][j] = true;
        this.elementCombo += 1;
        if (i>0 && i<5 && !alreadyUsed[i-1][j] && equal.nColorsCheck(new Card[]{board[i][j], board[i-1][j]},1,1))
            this.recursiveUsed (board, i-1, j);
        if (j>0 && j<6 && !alreadyUsed[i][j-1] && equal.nColorsCheck(new Card[]{board[i][j], board[i][j-1]},1 ,1))
            this.recursiveUsed (board, i, j-1);
        if (i+1<5 && !alreadyUsed[i+1][j] &&  equal.nColorsCheck(new Card[]{board[i][j], board[i+1][j]}, 1,1))
            this.recursiveUsed (board, i+1, j);
        if (j+1<6 && !alreadyUsed[i][j+1] && equal.nColorsCheck(new Card[]{board[i][j], board[i][j+1]},1,1))
            this.recursiveUsed (board, i, j+1);
    }
}

