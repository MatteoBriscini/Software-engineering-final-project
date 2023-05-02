package it.polimi.ingsw.client.Game;

import it.polimi.ingsw.client.Exceptions.InvalidPickException;
import it.polimi.ingsw.shared.Cards.*;
import it.polimi.ingsw.shared.JsonSupportClasses.*;

import java.util.Arrays;
import java.util.Comparator;

import static it.polimi.ingsw.shared.Cards.CardColor.EMPTY;


public class MainBoard {
    Card[][] board;
    private int rows;
    private int columns;
    public MainBoard(Card[][] board){
        this.board = board;
        this.columns = board.length;
        this.rows = board[0].length;
    }
    public Card[][] getBoard(){
        Card[][] tmpBoard=new Card[columns][rows];

        for(int x=0;x<columns;x++) {
            for (int y = 0; y < rows; y++) {
                tmpBoard[x][y] = new Card(board[x][y].getColor());
            }
        }
        return tmpBoard;
    }
    public CardColor getColor(int x, int y){
        return board[x][y].getColor();
    }

    public int getSketch(int x, int y){
        return board[x][y].getSketch();
    }


    /**
     * @param positions contains all the usable positions on the board
     * @throws InvalidPickException if one or more tiles do not have at least one side free, the pick is neither a row nor a column, or the tiles are not adjacent
     */
    public void validPick(Position[] positions) throws InvalidPickException {
        int valid=1;

        /** check by rows **/
        Arrays.asList(positions).sort(Comparator.comparing(Position::getX));
        for( int i = 0; i< positions.length && valid==1;i++){
            if(positions[i].getX()-positions[0].getX()!=i || positions[i].getY()!=positions[0].getY())
                valid = 0;

            if(!positions[i].pickable(board)){
                throw new it.polimi.ingsw.client.Exceptions.InvalidPickException("One or more tiles do not have one side free");
                }
        }
        if(valid==0) {

            /** check by columns **/
            Arrays.asList(positions).sort(Comparator.comparing(Position::getY));
            for (int i = 0; i < positions.length; i++) {
                if (positions[i].getY() - positions[0].getY() != i || positions[i].getX()!=positions[0].getX())
                    throw new it.polimi.ingsw.client.Exceptions.InvalidPickException("The pick is neither a row nor a column, or the tiles are not adjacent");

                if(!positions[i].pickable(board))
                    throw new it.polimi.ingsw.client.Exceptions.InvalidPickException("One or more tile does not have one side free");
            }
        }
    }

    /**
     * @param positions contains the positions of the cards that have to me removed from the board
     * This method substitute the removed cards with EMPTY cards
     */
    public void removeCard(PositionWithColor[] positions){
        for(PositionWithColor p: positions)
            board[p.getX()][p.getY()]=new Card(EMPTY);
    }


}
