package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

import static it.polimi.ingsw.Cards.CardColor.EMPTY;

public class StairsPatternTarget extends GroupTarget{

    public boolean check(Card[][] board){
        int x=0;
        int y=5;
        while(true){
            if(!(board[x][y].getColor().equals(EMPTY)) && board[x+1][y].getColor().equals(EMPTY)){
                if(x<4){
                    x++;
                    y--;
                }
                else
                    return true;
            } else
                if (x==0 && y==5) {
                    y--;
                }
                else{
                    x=0;
                    y=0;
                    while(true){
                        if(!(board[x][y].getColor().equals(EMPTY))){
                            if(board[x][y+1].getColor().equals(EMPTY)){
                                if(x<4){
                                    x++;
                                    y++;
                                }
                                else return true;
                            }
                            else{
                                if(x==0 && y==0)
                                    y++;
                                else return false;
                            }
                        }
                        else return false;
                    }
                }
        }
    };

}
