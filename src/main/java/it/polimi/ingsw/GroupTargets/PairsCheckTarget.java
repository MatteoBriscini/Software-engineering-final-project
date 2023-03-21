package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

public class PairsCheckTarget extends EqualTarget{
    private boolean[][] justUsed = new boolean[5][6];
    private int validCombo = 0;  //amount of valid pair
    public boolean check(Card[][] board){
        int i, j;
        for (i=0; i<5; i++){
            for (j=0; j<6; j++){
                if (allEqual(new Card[]{board[i][j], board[i+1][j]}) || allEqual(new Card[]{board[i][j], board[i][j+1]})) { //search for first pair
                    validCombo += 1; //increase pairs numbers
                    this.used(board, i, j);     //call method to save just used position
                }
            }
        }
        return (validCombo >= 6);
    }

    private void used(Card[][] board,int i,int j){  //recursive method, mark all adjacencies
        justUsed[i][j] = true;
        if (i>0 && allEqual(new Card[]{board[i][j], board[i-1][j]})) this.used(board, i-1, j);
        if (j>0 && allEqual(new Card[]{board[i][j], board[i][j-1]})) this.used(board, i, j-1);
        if (i+1<5 && allEqual(new Card[]{board[i][j], board[i+1][j]})) this.used(board, i+1, j);
        if (j+1<4 && allEqual(new Card[]{board[i][j], board[i][j+1]})) this.used(board, i, j+1);
    }

}
