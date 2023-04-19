package it.polimi.ingsw.Client.Game;

import it.polimi.ingsw.Client.Exceptions.InvalidPickException;
import it.polimi.ingsw.Shared.Cards.*;
import it.polimi.ingsw.Shared.JsonSupportClasses.*;

import java.util.Arrays;
import java.util.Comparator;

import static it.polimi.ingsw.Shared.Cards.CardColor.EMPTY;


public class MainBoard {
    Card[][] board;

    public MainBoard(Card[][] board){
        this.board = board;
    }
    public CardColor getColor(int x, int y){
        return board[x][y].getColor();
    }

    public int getSketch(int x, int y){
        return board[x][y].getSketch();
    }


    public void validPick(Position[] positions) throws InvalidPickException {
        int valid=1;

        Arrays.asList(positions).sort(Comparator.comparing(Position::getX));
        for( int i = 0; i< positions.length && valid==1;i++){
            if(positions[i].getX()-positions[0].getX()!=i || positions[i].getY()!=positions[0].getY())
                valid = 0;

            if(!positions[i].pickable(board))
                throw new it.polimi.ingsw.Client.Exceptions.InvalidPickException("One or more tiles do not have one side free");


        }
        if(valid==0) {
            Arrays.asList(positions).sort(Comparator.comparing(Position::getY));
            for (int i = 0; i < positions.length; i++) {
                if (positions[i].getY() - positions[0].getY() != i || positions[i].getX()!=positions[0].getX())
                    throw new it.polimi.ingsw.Client.Exceptions.InvalidPickException("The pick is neither a row nor a column, or the tiles are not adjacent");

                if(!positions[i].pickable(board))
                    throw new it.polimi.ingsw.Client.Exceptions.InvalidPickException("One or more tile does not have one side free");
            }
        }
    }

    public void removeCard(PositionWithColor[] positions){
        for(PositionWithColor p: positions)
            board[p.getX()][p.getY()]=new Card(EMPTY);
    }


}
