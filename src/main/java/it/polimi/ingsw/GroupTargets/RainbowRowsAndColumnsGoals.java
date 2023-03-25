package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Exceptions.CostructorExeception;
import it.polimi.ingsw.SupportClasses.NColorGroup;

/**
 * this class checks the following common goals:
 * three columns each formed by 6 tiles of maximum 3 different types,
 * four rows each formed by 5 tiles of maximum 3 different types,
 * two columns each formed by 6 different types of tiles,
 * two rows each formed by 5 different types of tiles,
 */
public class RainbowRowsAndColumnsGoals extends CommonGoal{
    private int n,min,max,tot;
    private final NColorGroup nColor = new NColorGroup();

    /**
     * @param n is the number of Cards required from the goal
     * @param min is the minimum number of different colours required from the goal
     * @param max is the maximum number of different colours required from the goal
     * @param tot is the number of rows or columns that must fulfill the requirement of the number of colors
     */
    public RainbowRowsAndColumnsGoals(int n, int min, int max, int tot) throws CostructorExeception{
        if((n==6 && ((min==1 && max==3 && tot==4) || (min==5 && max==5 && tot==2))) || (n==5 && ((min==1 && max==3 && tot==3) || (min==6 && max==6 && tot==2))) ) {
            this.n = n;
            this.min = min;
            this.max = max;
            this.tot = tot;
        }
        else throw new CostructorExeception("invalid parameter for RainbowRowsAndColumnsGoals constructor");


    }

    /**
     * @param board is a matrix that represents the main board
     * @return true if the goal has been reached, false otherwise
     */
    public boolean check(Card board[][]){
        int tmptot = tot;

        /** the rows are made by 5 Cards, while the columns is made by 6 Cards.
         ** depending on the value of n, this loop checks how many rows or columns fulfill the requirements of the goal
         ** (if n==6 the check is made on the rows, if n==5 it is made on the columns
         * */
        for(int i=0;i<n;i++) {
            if((n==6 && (nColor.nColorsCheck(new Card[]{board[0][i], board[1][i], board[2][i], board[3][i], board[4][i]}, min, max))) || (n==5 && (nColor.nColorsCheck(new Card[]{board[i][0], board[i][1], board[i][2], board[i][3], board[i][4], board[i][5]}, min, max)))) {
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
