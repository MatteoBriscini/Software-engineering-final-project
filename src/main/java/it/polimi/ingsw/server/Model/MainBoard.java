package it.polimi.ingsw.server.Model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.Cards.CardColor;
import it.polimi.ingsw.server.Exceptions.InvalidPickException;
import it.polimi.ingsw.Shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.Shared.JsonSupportClasses.Position;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.io.*;
import java.util.*;

import static it.polimi.ingsw.Shared.Cards.CardColor.*;

/**
 * This class is used to make all the checks on the MainBoard and remove or insert the cards
 */
public class MainBoard {

    private final List<Card> colorsList;
    private final Card[][] board;
    Random rand = new Random();
    private JsonUrl jsonUrl;
    private int nColors;
    private int cardsByColor;
    private int rows;
    private int columns;



    private void jsonCreate() throws FileNotFoundException {
        Gson gson = new Gson();

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jsonUrl.getUrl("mainBoardConfig"));
        System.out.println(inputStream);
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        JsonObject controller = new Gson().fromJson(bufferedReader, JsonObject.class);

        this.nColors = controller.get("nColors").getAsInt();
        this.cardsByColor = controller.get("cardsByColor").getAsInt();
        this.columns = controller.get("columns").getAsInt();
        this.rows = controller.get("rows").getAsInt();
    }

    /**
     * Creates a new bord (take the sizes from the Json configuration file) and fills it with EMPTY cards
     * Creates and fill colorList with the required cards (numbers of colors and cards by color are specified into the Json file)
     */
    public MainBoard(){

        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            System.out.println("MainBoard: JSON FILE NOT FOUND");
        }


        this.board = new Card[columns][rows];

        fillWithEmpty();

        colorsList=new ArrayList<>();
        fillColorsList();
    }

    /**
     * this method fills the board with Empty cards
     */
    private void fillWithEmpty(){
        for(int x=0; x<columns; x++)
            for(int y=0; y<rows;y++)
                board[x][y]=new Card(EMPTY);
    }

    /**
     * this method fills the list of colors (that represents the bag) with the required number of cards of each color
     */
    private void fillColorsList(){
        for(int i=0;i<nColors;i++) {
            for (int j = 0; j < cardsByColor; j++) {
                colorsList.add(new Card(CardColor.values()[i]));
            }
        }
    }

    /**
     * @return a copy of the board
     */
    public Card[][] getBoard() {
        Card[][] tmpBoard=new Card[columns][rows];

        for(int x=0;x<columns;x++) {
            for (int y = 0; y < rows; y++) {
                tmpBoard[x][y] = new Card(board[x][y].getColor());
            }
        }
        return tmpBoard;
    }

    /**
     * @param positions is the array of the allowed positions on the board
     * @return false if there are no cards left in the bag, true otherwise
     */
    public boolean fillBoard(Position[] positions){
        boolean remainingCards=false;

        /** substitute all the cards with EMPTY cards and add them to the colors list **/
            for (Position a : positions) {
                if (!board[a.getX()][a.getY()].getColor().equals(EMPTY)) {
                    PositionWithColor p = new PositionWithColor(a.getX(), a.getY(), 0, board[a.getX()][a.getY()].getColor());
                    removeAndUpdate(new PositionWithColor[]{p});
                }
            }

            /** insert new cards in the board **/
            for(Position a : positions){
                if(colorsList.size()>0) {
                    remainingCards = true;
                    insert(a);
                }
                else
                    break;
            }
        return remainingCards;
    }

    /**
     * @param positions
     * @return true if there are no moves left AFTER the card has been removed, false otherwise
     * @throws InvalidPickException if the pick is not valid [check the validPick method JavaDoc]
     */
    public boolean removeCard(PositionWithColor[] positions) throws InvalidPickException{

        validPick(positions);

        for(PositionWithColor p : positions)
            board[p.getX()][p.getY()]=new Card(EMPTY);

        return noMovesLeft();

    }

    /**
     * @param positions contains the information about the position and the color of the card that has to be checked
     * @throws InvalidPickException if the client board is deprecated, one or more tiles do not have one side free or the pick is neither a row nor a column, or the tiles are not adjacent
     */
    private void validPick(PositionWithColor[] positions) throws InvalidPickException{
        int valid=1;

        /** check by rows **/
        Arrays.asList(positions).sort(Comparator.comparing(Position::getX));
        for( int i = 0; i< positions.length && valid==1;i++){
            if(positions[i].getX()-positions[0].getX()!=i || positions[i].getY()!=positions[0].getY())
                valid = 0;

            if(deprecatedBoard(positions[i]))
                throw new InvalidPickException("Client board is deprecated");


            if(!positions[i].pickable(board))
                throw new InvalidPickException("One or more tiles do not have one side free");


        }
        if(valid==0) {

            /** check by columns **/
            Arrays.asList(positions).sort(Comparator.comparing(Position::getY));
            for (int i = 0; i < positions.length; i++) {
                if (positions[i].getY() - positions[0].getY() != i || positions[i].getX()!=positions[0].getX())
                    throw new InvalidPickException("The pick is neither a row nor a column, or the tiles are not adjacent");

                if(deprecatedBoard(positions[i]))
                    throw new InvalidPickException("Client board is deprecated");


                if(!positions[i].pickable(board))
                    throw new InvalidPickException("One or more tile does not have one side free");
            }
        }
    }

    /**
     * @param position contains the information about the position and the color of the card that has to be checked
     * @return true if the color of the selected card and the one of the expected card don't match
     */
    private boolean deprecatedBoard(PositionWithColor position){
        return(!(board[position.getX()][position.getY()].getColor().equals(position.getColor())));
    }

    /**
     * @return true if there are no moves left on the board, false otherwise
     */
    private boolean noMovesLeft(){
        for(int x=0; x<columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (x < (columns-1) && !board[x][y].getColor().equals(EMPTY) && !board[x + 1][y].getColor().equals(EMPTY))
                    return false;
                if (y < (rows-1) && !board[x][y].getColor().equals(EMPTY) && !board[x][y + 1].getColor().equals(EMPTY))
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
    private void insert(Position p){
            Card c=colorsList.get(rand.nextInt(colorsList.size()));
            board[p.getX()][p.getY()] = c;

            colorsList.remove(c);

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