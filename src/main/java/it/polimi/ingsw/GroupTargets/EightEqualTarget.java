package it.polimi.ingsw.GroupTargets;

import it.polimi.ingsw.Cards.Card;

import static it.polimi.ingsw.Cards.CardColor.*;

//this class is used to check that there are 8 cards having the same color on the player's board
public class EightEqualTarget extends CommonGoal {

    public boolean check(Card[][] board){
        int[] color=new int[6]; //this array is used to count the occurrences of each color (the order is the same of the enum in CardColor)
        int x=0;
        int y=0;
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
                        color[0]++;
                        if(color[0]==8)
                            return true;
                    }
                    case GREEN: {
                        color[1]++;
                        if(color[1]==8)
                            return true;
                    }
                    case LIGHTBLUE: {
                        color[2]++;
                        if(color[2]==8)
                            return true;
                    }
                    case PINK: {
                        color[3]++;
                        if(color[3]==8)
                            return true;
                    }
                    case WHITE: {
                        color[4]++;
                        if(color[4]==8)
                            return true;
                    }
                    case YELLOW: {
                        color[5]++;
                        if(color[5]==8)
                            return true;
                    }
                }
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
