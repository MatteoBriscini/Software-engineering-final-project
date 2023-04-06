package it.polimi.ingsw.Server.Model.GroupGoals;

import it.polimi.ingsw.Shared.Cards.Card;

import static it.polimi.ingsw.Shared.Cards.CardColor.EMPTY;

public class StairsPatternGoal extends CommonGoal {

    public boolean check(Card[][] board){
        int x=0;
        int y=5;
        while(true){
            if(!(board[x][y].getColor().equals(EMPTY)) && (x==4 || board[x+1][y].getColor().equals(EMPTY))){
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
                            if(y==5 || board[x][y+1].getColor().equals(EMPTY)){
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
