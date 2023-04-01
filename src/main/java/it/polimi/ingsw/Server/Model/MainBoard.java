package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.Cards.Card;
import it.polimi.ingsw.Server.Model.Cards.CardColor;
import it.polimi.ingsw.Server.Exceptions.InvalidPickException;
import it.polimi.ingsw.Server.JsonSupportClasses.Position;
import it.polimi.ingsw.Server.JsonSupportClasses.PositionWithColor;

import java.util.*;

import static it.polimi.ingsw.Server.Model.Cards.CardColor.*;

/**
 * this class is used to make all the checks on the MainBoard, removing or inserting the cards
 */
public class MainBoard {

    private List<Card> colorsList;

    private Card board[][];

    Random rand = new Random();


    public MainBoard(){
        this.board = new Card[9][9];

        fillWithEmpty();

        colorsList=new ArrayList<>();
        fillColorsList();
    }

    /**
     * this method fills the board with Empty cards
     */
    private void fillWithEmpty(){
        for(int x=0; x<9; x++)
            for(int y=0; y<9;y++)
                board[x][y]=new Card(EMPTY);
    }

    /**
     * this method fills the list of colors (that represents the bag) with the required number of cards of each color
     */
    private void fillColorsList(){
        for(int i=0;i<6;i++) {
            for (int j = 0; j < 22; j++) {
                colorsList.add(new Card(CardColor.values()[i]));
            }
        }
    }

    public Card[][] getBoard() {
        return board;



        //ritornare copia di board, non board
    }

    /**
     * @param positions is the array of the allowed positions on the board
     * @return false if there are no cards left in the bag, true otherwise
     */
    public boolean fillBoard(Position[] positions){
        boolean end;

        for( Position a : positions){
            if(!board[a.getX()][a.getY()].getColor().equals(EMPTY)){
                PositionWithColor p= new PositionWithColor(a.getX(),a.getY(),0,board[a.getX()][a.getY()].getColor());
                try {
                    removeCard(new PositionWithColor[]{p});
                }catch (InvalidPickException e){}
            }
            end=insert(a);
            if(!end)
                return end;
        }
        return true;
    }

    /**
     * @param positions
     * @return true if there are no moves left AFTER the card has been removed, false otherwise
     * @throws InvalidPickException if the pick is not valid [check the validPick method JavaDoc]
     */
    public boolean removeCard(PositionWithColor[] positions) throws InvalidPickException{

        validPick(positions);

        removeAndUpdate(positions);

        return noMovesLeft();

    }

    /**
     * @param positions contains the information about the position and the color of the card that has to be checked
     * @throws InvalidPickException if the client board is deprecated, one or more tiles do not have one side free or the pick is neither a row nor a column, or the tiles are not adjacent
     */
    private void validPick(PositionWithColor[] positions) throws InvalidPickException{
        int valid=1;

        Arrays.asList(positions).sort(Comparator.comparing(Position::getX));
        for( int i = 0; i< positions.length && valid==1;i++){
            if(positions[i].getX()-positions[0].getX()!=i || positions[i].getY()!=positions[0].getY())
                valid = 0;

            if(DeprecatedBoard(positions[i]))
                throw new InvalidPickException("Client board is deprecated");

            if(!freeAdjacency(positions[i]))
                throw new InvalidPickException("One or more tiles do not have one side free");

        }
        if(valid==0) {
            Arrays.asList(positions).sort(Comparator.comparing(Position::getY));
            for (int i = 0; i < positions.length; i++) {
                if (positions[i].getY() - positions[0].getY() != i || positions[i].getX()!=positions[0].getX())
                    throw new InvalidPickException("The pick is neither a row nor a column, or the tiles are not adjacent");

                if(DeprecatedBoard(positions[i]))
                    throw new InvalidPickException("Client board is deprecated");

                if(!freeAdjacency(positions[i]))
                    throw new InvalidPickException("One or more tile does not have one side free");
            }
        }
    }

    /**
     * @param position contains the information about the position and the color of the card that has to be checked
     * @return true if the color of the selected card and the one of the expected card don't match
     */
    private boolean DeprecatedBoard(PositionWithColor position){
        return(!(board[position.getX()][position.getY()].getColor().equals(position.getColor())));
    }

    /**
     * @param position is the position of the card to check
     * @return true if there is a free adjacency from the selected card, false otherwise
     */
    private boolean freeAdjacency(PositionWithColor position){
        if(!(position.getX()==0) && !(position.getX()==4) && !(position.getY()==0)  && !(position.getY()==5))
            return((board[position.getX()-1][position.getY()].getColor().equals(EMPTY)) || (board[position.getX()][position.getY()-1].getColor().equals(EMPTY)) ||(board[position.getX()+1][position.getY()].getColor().equals(EMPTY)) || (board[position.getX()][position.getY()+1].getColor().equals(EMPTY)));
        return true;
    }

    /**
     * @return true if there are no moves left on the board, false otherwise
     */
    private boolean noMovesLeft(){
        for(int x=0; x<5; x++) {
            for (int y = 0; y < 6; y++) {
                if (x < 4 && !board[x][y].getColor().equals(EMPTY) && !board[x + 1][y].getColor().equals(EMPTY))
                    return false;
                if (y < 5 && !board[x][y].getColor().equals(EMPTY) && !board[x][y + 1].getColor().equals(EMPTY))
                    return false;
            }
        }
        return true;
    }

    /**
     * @param positions is the position from where the card has to be removed
     *
     * this method puts an empty card in the selected position and add the removed card to the bag
     */
    private void removeAndUpdate(PositionWithColor[] positions){
        for(PositionWithColor p : positions){
            board[p.getX()][p.getY()]=new Card(EMPTY);
            colorsList.add(new Card(p.getColor()));
        }
    }

    /**
     * @param p is the position to fill with the card
     * @return true if there are still cards in the bag BEFORE filling the position, false otherwise
     *
     * this method picks a random card from the bag, put it in the selected position on the board and then removes the
     * card from the list of available cards
     */
    private boolean insert(Position p){
        if(colorsList.size()>0) {
            Card c=colorsList.get(rand.nextInt(colorsList.size()));
            board[p.getX()][p.getY()] = c;
            colorsList.remove(c);
            return true;
        }
        return false;
    }


    /**
     * @param position contains the positions and colors of the cards that have to be re-added to the boar
     */
    public void fixBoard(PositionWithColor[] position){
        for(PositionWithColor p : position){
            board[p.getX()][p.getY()] = new Card(p.getColor());
            colorsList.remove(p.getColor());
        }
    }
}