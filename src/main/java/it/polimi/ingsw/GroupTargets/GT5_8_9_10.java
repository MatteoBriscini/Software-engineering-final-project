package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

/**
 * this class checks the common goals number 5, 8, 9 and 10
 * [following the order of the italian rulebook, reading from top to down, from the left to the right]
 */
public class GT5_8_9_10 extends NElementsCheck{
    private int n,min,max,tot;

    /**
     * @param n is the number of Cards required from the goal
     * @param min is the minimum number of different colours required from the goal
     * @param max is the maximum number of different colours required from the goal
     * @param tot is the number of rows or columns that must fulfill the requirement of the number of colors
     */
    public GT5_8_9_10(int n, int min, int max, int tot){
        this.n = n;
        this.min = min;
        this.max = max;
        this.tot = tot;
    }

    /**
     * @param board is a matrix that represents the main board
     * @return true if the goal has been reached, false otherwise
     */
    public boolean check(Card board[][]){
        int tmptot = tot;

        /** the rows are made by 5 Cards, while the columns is made by 6 Cards.
         ** depending on the value of n, this loop checks how many rows or columns fulfill the requirements of the goal*/
        for(int i=0;i<n;i++) {
            if((n==6 && (this.nColorsCheck(new Card[]{board[0][i], board[1][i], board[2][i], board[3][i], board[4][i]}, min, max))) || (n==5 && (this.nColorsCheck(new Card[]{board[i][0], board[i][1], board[i][2], board[i][3], board[i][4], board[i][5]}, min, max)))) {
                {
                    tmptot--;
                }
                if(tmptot==0) {
                    return true;
                }
            }
        }
        return false;
    }
}
