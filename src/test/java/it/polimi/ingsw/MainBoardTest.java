package it.polimi.ingsw;

import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.server.Exceptions.InvalidPickException;
import it.polimi.ingsw.Shared.JsonSupportClasses.Position;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.server.Model.MainBoard;
import junit.framework.TestCase;

import java.util.List;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;

public class MainBoardTest extends TestCase {

    private List<Card> colorsList;

    private MainBoard mboard=new MainBoard();

    private Card[][] board;

    private PositionWithColor rm[]= new PositionWithColor[1];
    private PositionWithColor rm2[] = new PositionWithColor[2];

    private PositionWithColor rm3[] = new PositionWithColor[3];
    boolean bool;


    private Position position[]={new Position(4,1),new Position(5,1),   new Position(3,2),new Position(4,2),new Position(5,2),   new Position(1,3),new Position(2,3),new Position(3,3),new Position(4,3),new Position(5,3),new Position(6,3),    new Position(1,4),new Position(2,4),new Position(3,4),new Position(4,4),new Position(5,4),new Position(6,4), new Position(7,4),   new Position(2,5),new Position(3,5),new Position(4,5),new Position(5,5),new Position(6,5), new Position(7,5),    new Position(3,6),new Position(4,6),new Position(5,6),   new Position(3,7),new Position(4,7) };


    public void testGetBoard() {
        board=mboard.getBoard();
        System.out.println("\n\n\n\n\n");
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
    }

    public void testFillBoard() {
        bool=mboard.fillBoard(position);
        assert bool;
        board=mboard.getBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }

        rm2[0]=new PositionWithColor(4,1,0,board[4][1].getColor());
        rm2[1]= new PositionWithColor(5,1,0,board[5][1].getColor());

        try {
            mboard.removeCard(rm2);
        }catch (InvalidPickException e){
            assert false;
        }


        System.out.println("\n\n\n\n\n");
        bool=mboard.fillBoard(position);
        assert bool;
        board=mboard.getBoard();
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }
    }


    public void testRemoveCard() {

        bool=mboard.fillBoard(position);
        board=mboard.getBoard();
        rm[0]=new PositionWithColor(4,1,0,board[4][1].getColor());

        try {
            mboard.removeCard(rm);
        }catch (InvalidPickException e){
            assert false;
        }

        board=mboard.getBoard();
        System.out.println("\n\n\n\n\n");
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }



        rm[0]=new PositionWithColor(4,1,0, BLUE);
        System.out.println("\n\n\\\\\n"+board[4][1].getColor().toString());

        bool = false;
        try {
            mboard.removeCard(rm);
        }catch (InvalidPickException e){
            bool = true;
        }
        assert bool;

        board=mboard.getBoard();
        System.out.println("\n\n\n\n\n");
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }

        mboard=new MainBoard();

        bool=mboard.fillBoard(position);
        bool=false;
        board=mboard.getBoard();
        rm2[0]=new PositionWithColor(4,1,0,board[4][1].getColor());
        rm2[1]=new PositionWithColor(5,5,0,board[5][5].getColor());

        try {
            mboard.removeCard(rm2);
        }catch (InvalidPickException e){
            bool = true;
        }
        assert (bool);

        System.out.println("fatto");
        board=mboard.getBoard();
        System.out.println("\n\n\n\n\n");
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }


        mboard=new MainBoard();

        bool=mboard.fillBoard(position);
        board=mboard.getBoard();
        rm3[0]=new PositionWithColor(4,1,0,board[4][1].getColor());
        rm3[1]=new PositionWithColor(5,5,0,board[5][5].getColor());
        rm3[2]=new PositionWithColor(1,4,0,board[1][4].getColor());

        bool = false;
        try {
            mboard.removeCard(rm2);
        }catch (InvalidPickException e){
            bool = true;
        }
        assert bool;

        board=mboard.getBoard();
        System.out.println("\n\n\n\n\n");
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }


        bool=mboard.fillBoard(position);
        bool=false;
        board=mboard.getBoard();
        rm2[0]=new PositionWithColor(4,1,0,board[4][1].getColor());
        rm2[1]=new PositionWithColor(5,5,0,board[5][5].getColor());

        try {
            mboard.removeCard(rm2);
        }catch (InvalidPickException e){
            bool = true;
        }
        assert (bool);

        System.out.println("fatto");
        board=mboard.getBoard();
        System.out.println("\n\n\n\n\n");
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }


        mboard=new MainBoard();

        bool=mboard.fillBoard(position);
        board=mboard.getBoard();
        rm3[0]=new PositionWithColor(4,1,0,board[4][1].getColor());
        rm3[1]=new PositionWithColor(5,5,0,board[5][5].getColor());
        rm3[2]=new PositionWithColor(1,4,0,board[1][4].getColor());

        bool = false;
        try {
            mboard.removeCard(rm2);
        }catch (InvalidPickException e){
            bool = true;
        }
        assert bool;

        board=mboard.getBoard();
        System.out.println("\n\n\n\n\n");
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }


    }


    public void testFixBoard() {
        rm2[0]=new PositionWithColor(4,1,0,LIGHTBLUE);
        rm2[1]=new PositionWithColor(5,5,0,WHITE);

        mboard.fixBoard(rm2);
        board=mboard.getBoard();
        System.out.println("\n\n\n\n\n");
        for(int y=8;y>=0;y--){
            System.out.println(board[0][y].getColor().toString()+"\t"+board[1][y].getColor().toString()+"\t"+board[2][y].getColor().toString()+"\t"+board[3][y].getColor().toString()+"\t"+board[4][y].getColor().toString()+"\t"+board[5][y].getColor().toString()+"\t"+board[6][y].getColor().toString()+"\t"+board[7][y].getColor().toString()+"\t"+board[8][y].getColor().toString());
        }

    }
}