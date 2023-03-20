package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

public class QuadrupletTarget extends EqualTarget{

    private boolean[][] justUsed = new boolean[5][6];
    private int validCombo = 0;  //amount of valid row
    private int elementCombo = 0;  //amount of element in a single combo

    public boolean check(Card[][] board){
        int i,j;
        for (i=0; i<5; i++){
            for (j=0; j<6; j++){
                if (allEqual(new Card[]{board[i][j], board[i+1][j]}) || allEqual(new Card[]{board[i][j], board[i][j+1]})) { //search for first pair
                    this.used(board, i, j);     //call method to save just used position
                    if (elementCombo >= 4) validCombo += 1;
                    elementCombo = 0;
                }
            }
        }
        return validCombo >= 4;
    }

    private void used (Card[][] board,int i,int j){
        justUsed[i][j] = true;
        elementCombo += 1;
        if (i>0 && allEqual(new Card[]{board[i][j], board[i-1][j]})) this.used(board, i-1, j);
        if (j>0 && allEqual(new Card[]{board[i][j], board[i][j-1]})) this.used(board, i, j-1);
        if (i+1<5 && allEqual(new Card[]{board[i][j], board[i+1][j]})) this.used(board, i+1, j);
        if (j+1<4 && allEqual(new Card[]{board[i][j], board[i][j+1]})) this.used(board, i, j+1);
    }

}
