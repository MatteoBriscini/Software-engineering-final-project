package it.polimi.ingsw.GroupTargets;
import it.polimi.ingsw.Cards.Card;
public class DiagonAlleyTarget extends EqualTarget {  //easter egg :)

    private Card[] D0 = new Card[5];
    private Card[] D1 = new Card[5];
    private Card[] D2 = new Card[5];
    private Card[] D3 = new Card[5];
    public boolean check(Card[][] board){
        int i;
        for (i=0; i<5; i++){        //built array from the diagonal
            D0[i] = board[i][i];
            D1[i] = board[i][i+1];
            D2[i] = board[4-i][i];
            D3[i] = board[4-i][i+1];
        }
        return  (allEqual(D0) || allEqual(D1) || allEqual(D2) || allEqual(D3));
    }

}
