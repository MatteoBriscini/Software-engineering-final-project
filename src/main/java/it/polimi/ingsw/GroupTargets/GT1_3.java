package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

public class GT1_3 extends EqualTarget{

    private boolean[][] alreadyUsed = new boolean[5][6];
    private int validCombo = 0;  //amount of valid row
    private int elementCombo = 0;  //amount of element in a single combo

    private int n, mGroups; //value to distinguish different case (t1 and t3)
    //n is number of element in one group, mGroups is the number of groups required to reach the goal
    public GT1_3 (int n, int mGroups){ //possible value for n (4 o 2);
        this.n = n;
        this.mGroups = mGroups;
    }

    public boolean check(Card[][] board){
        int i,j;
        for (i=0; i<4; i++){
            for (j=0; j<5; j++){
                if (!alreadyUsed[i][j] &&( allEqual(new Card[]{board[i][j], board[i+1][j]}) || allEqual(new Card[]{board[i][j], board[i][j+1]}))) { //search for first pair
                    this.used(board, i, j);     //call method to save just used position
                    if (elementCombo >= n) validCombo += 1;
                    elementCombo = 0;
                }
            }
        }
        return validCombo >= 4;
    }

    private void used (Card[][] board,int i,int j){
        alreadyUsed[i][j] = true;
        elementCombo += 1;
        if (i>0 && allEqual(new Card[]{board[i][j], board[i-1][j]})) this.used(board, i-1, j);
        if (j>0 && allEqual(new Card[]{board[i][j], board[i][j-1]})) this.used(board, i, j-1);
        if (i+1<5 && allEqual(new Card[]{board[i][j], board[i+1][j]})) this.used(board, i+1, j);
        if (j+1<4 && allEqual(new Card[]{board[i][j], board[i][j+1]})) this.used(board, i, j+1);
    }

}
