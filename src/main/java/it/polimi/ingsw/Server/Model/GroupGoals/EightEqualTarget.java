package it.polimi.ingsw.Server.Model.GroupGoals;

import it.polimi.ingsw.Shared.Cards.Card;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;

//this class is used to check that there are 8 cards having the same color on the player's board
public class EightEqualTarget extends CommonGoal {

    public boolean check(Card[][] board){
        int[] color=new int[6]; //this array is used to count the occurrences of each color (the order is the same of the enum in CardColor)
        int x=0;
        int y=0;
        int i=0;
        while(true){

            if(board[x][y].getColor().equals(EMPTY)){ //if the cell is empty, the cells above it are empty too
                if(x<4){ //if the method hasn't checked the last column, it checks the next one
                    x++;
                    y=0;
                }
                else return false; //if the method reach this point, it means that it has checked all the columns without counting 8 cards with the same color
            }
            else{
                switch(board[x][y].getColor()){ //this switch-case is used to count the occurrence of the card's color, it does return TRUE if the current occurrence is the eighth one
                    case BLUE: {
                        i=0;
                        break;
                    }
                    case GREEN: {
                        i=1;
                        break;
                    }
                    case LIGHTBLUE: {
                        i=2;
                        break;
                    }
                    case PINK: {
                        i=3;
                        break;
                    }
                    case WHITE: {
                        i=4;
                        break;
                    }
                    case YELLOW: {
                        i=5;
                        break;
                    }
                }
                color[i]++;
                if(color[i]==8)
                    return true;
                if(y<5) //if the method hasn't checked all the rows of the curren column, it has to check the next row, otherwise it has to check the next column from the bottom
                    y++;
                else
                    if(x<4){
                        x++;
                        y=0;
                    }
                    else
                        return false; //if the method reaches this point, it means that the whole board has been checked without reaching the target
            }

        }
    };

}
