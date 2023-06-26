package it.polimi.ingsw.gui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Exceptions.PlayerNotFoundException;
import it.polimi.ingsw.client.Game.PlayerBoard;
import it.polimi.ingsw.gui.supportClass.*;
import it.polimi.ingsw.server.Model.MainBoard;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Cards.CardColor;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.Position;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static it.polimi.ingsw.shared.Cards.CardColor.BLUE;
import static it.polimi.ingsw.shared.Cards.CardColor.EMPTY;
import static javafx.geometry.Pos.CENTER;

public class GameController extends GuiView implements Initializable {



    //game

    @FXML
    private HBox bookshelfAnchor;
    private ArrayList<Node> othersBookshelf = new ArrayList<>();
    private PlayingPlayer player;
    private ArrayList<String> otherPlayers = new ArrayList<>();
    @FXML
    private Label currentPlayer = new Label();
    @FXML
    private AnchorPane livingRoomBox;
    @FXML
    private AnchorPane myBookshelfDiv;
    @FXML
    private AnchorPane livingRoomClickable = new AnchorPane();
    private ArrayList<Position> positions = new ArrayList<>();
    GridPane mainBoardGrid;
    GridPane myPlayerBoardGrid;
    @FXML
    private HBox myTails;
    @FXML
    private VBox myBookshelfBox2;
    @FXML
    private VBox myBookshelfBox3;

    ArrayList<Node> greyNode = new ArrayList<>();
    private Map<String, GridPane> otherPlayerBoardGrid = new HashMap<>();
    private AnchorPane[] otherPlayerBoardAnchors;
    private ImageView[] otherPlayerBoardBox;
    @FXML
    private AnchorPane bookshelfAnchor1;
    @FXML
    private AnchorPane bookshelfAnchor2;
    @FXML
    private AnchorPane bookshelfAnchor3;
    @FXML
    private TextField reorderMove;
    @FXML
    private TextField columnMove;
    //bookShelf
    @FXML
    private Label shelfID1 = new Label();
    @FXML
    private Label shelfID2 = new Label();
    @FXML
    private Label shelfID3 = new Label();

    //images

    @FXML
    public ImageView logo = new ImageView();
    @FXML
    private ImageView myBookshelfImage = new ImageView();
    @FXML
    private ImageView myBookshelfImage1 = new ImageView();
    @FXML
    private ImageView myBookshelfImage2 = new ImageView();
    @FXML
    private ImageView myBookshelfImage3 = new ImageView();
    @FXML
    private ImageView livingRoom = new ImageView();
    @FXML
    private ImageView personalGoal1 = new ImageView();
    @FXML
    private ImageView commonGoal1 = new ImageView();
    @FXML
    private ImageView commonGoal2 = new ImageView();
    @FXML
    private VBox commonGoalsScore = new VBox();

    //chat
    @FXML
    private ChoiceBox<String> chatName = new ChoiceBox<>();
    @FXML
    private ScrollPane messageContainer = new ScrollPane();
    @FXML
    private TextField chatMSG = new TextField();
    private boolean chatOpen = true;
    private ArrayList<Message> messages =new ArrayList<>();

    //ux
    @FXML
    private VBox rightDiv = new VBox();
    @FXML
    private VBox leftDiv = new VBox();
    @FXML
    private AnchorPane main = new AnchorPane();
    private AnchorPane menu;
    private MediaPlayer backgroundMusic;

    //setup data
    private int maxTakenCard;
    private int playerBoardRows;
    /**********************************************************************
     *                              initialize                            *
     **********************************************************************/
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {            //static element init
        imagesInit(myBookshelfImage, "bookshelf_orth.png"); //load player board img

        imagesInit(logo, "Publisher.png");

        imagesInit(livingRoom, "livingroom.png");       //load living room png
        this.setBorderRadius(livingRoom, 40);

        try {                                               //load config data
            jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.musicInit();
        this.musicStart();
    }

    private void jsonCreate() throws FileNotFoundException{
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("controllerConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader, JsonObject.class);
        this.maxTakenCard = jsonObject.get("maxTakeCard").getAsInt();

        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("playerBoardConfig"));
        if(inputStream1 == null) throw new FileNotFoundException();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
        JsonObject jsonObject1 = new Gson().fromJson(bufferedReader1, JsonObject.class);
        this.playerBoardRows = jsonObject1.get("x").getAsInt();
    }

    public void gameInit(){                         //dynamic element init
        player = (PlayingPlayer) helloApplication.getPlayer();
        chatInit();
        playerInit();
        commonGoalsInit();
        privateGoalsInit();
        setMainBoard();
        setMyPlayerBoardGrid();
        setOtherPlayerBoard();


        this.mouseCoordinates();
        this.notifyNewActivePlayer();
    }

    private void commonGoalsInit(){
        int[] goals = player.getCommonGoalID();
        ImageView[] commonGoal = new ImageView[2];
        commonGoal[0] = commonGoal1; commonGoal[1] = commonGoal2;
        for(int i=0;i<goals.length;i++){
            imagesInit(commonGoal[i], CommonGoal.getImgName(goals[i]));
            this.setBorderRadius(commonGoal[i], 40);
        }
    }
    private void privateGoalsInit(){
        try {
            String goalImg = PrivateGoal.getImgName(player.getPrivateGoal(), privateGoalsInitJson());
            imagesInit(personalGoal1, goalImg);
            this.setBorderRadius(personalGoal1, 40);
        } catch (FileNotFoundException e) {
            System.err.println("GAME CONTROLLER: can't find goal json");
        }
    }

    /**
     * @return initialization for common goals
     * @throws FileNotFoundException
     */
    private JsonArray privateGoalsInitJson() throws FileNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("playerTarget"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return new Gson().fromJson(bufferedReader, JsonArray.class);
    }
    private void playerInit(){
        for(String s: player.getPlayersID())if(!s.equals(player.getPlayerID()))otherPlayers.add(s);
        imagesInit(myBookshelfImage1, "bookshelf_orth.png");
        shelfID1.setText(otherPlayers.get(0));


        if(otherPlayers.size()>1) {
            imagesInit(myBookshelfImage2, "bookshelf_orth.png");
            shelfID2.setText(otherPlayers.get(1));
        } else bookshelfAnchor.getChildren().removeAll(myBookshelfBox2);
        if(otherPlayers.size()>2) {
            imagesInit(myBookshelfImage3, "bookshelf_orth.png");
            shelfID3.setText(otherPlayers.get(2));
        } else bookshelfAnchor.getChildren().removeAll(myBookshelfBox3);
    }

    /**
     * @param image used for the image
     * @param size used for the size on the gui
     */
    private void setBorderRadius (ImageView image, int size){
        Rectangle clip = new Rectangle(
                image.getFitWidth(), image.getFitHeight()
        );
        clip.setArcWidth(size);
        clip.setArcHeight(size);
        image.setClip(clip);
    }

    /**********************************************************************
     *                               GAME                                 *
     **********************************************************************/
    public Image createImage(int sketch, CardColor color, ArrayList<Title> titles){
        Title tmp = new Title(color , sketch);
        if(titles.contains(tmp)){
            for (Title title: titles)if(title.equals(tmp))return title.getImage();
        }
        String file = CardImage.getImgName(sketch,color);
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(file), 60, 60, false, false);
        titles.add(new Title(image, color, sketch));
        return image;
    }
    public void notifyNewActivePlayer() {
        String currentPlayer = player.getActivePlayer();
        if(!Objects.equals(currentPlayer, player.getPlayerID())){
            this.currentPlayer.setText("CURRENT PLAYER: \n" + currentPlayer);
            this.currentPlayer.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(0))));
        }
        else{
            this.currentPlayer.setText("YOUR TURN \n " + currentPlayer);
            this.currentPlayer.setBorder(new Border(new BorderStroke(Color.rgb(255, 255, 179), BorderStrokeStyle.SOLID, new CornerRadii(9), new BorderWidths(2))));
        }
    }
    public void setMainBoard(){

        ObservableList<Node> nodes = livingRoomBox.getChildren();

        for(Node n : nodes){
            if(n.getId().equals("cardGrid")){
                livingRoomBox.getChildren().remove(n);
                break;
            }
        }

        Node node = livingRoomBox.getChildren().get(2);
        mainBoardGrid = new GridPane();
        mainBoardGrid.setVgap(7);
        mainBoardGrid.setHgap(7);
        mainBoardGrid.setLayoutX(32);
        mainBoardGrid.setLayoutY(32);
        mainBoardGrid.setId("cardGrid");


        //TO FIX BOARD DIMENSION
        ImageView card = new ImageView(createImage(0, BLUE, helloApplication.getTitles()));
        card.setFitHeight(50);
        card.setFitWidth(50);
        card.setVisible(false);
        GridPane.setConstraints(card,0,0);
        mainBoardGrid.getChildren().add(card);
        card = new ImageView(createImage(0, BLUE, helloApplication.getTitles()));
        card.setFitHeight(50);
        card.setFitWidth(50);
        card.setVisible(false);
        GridPane.setConstraints(card,0,8);
        mainBoardGrid.getChildren().add(card);
        card = new ImageView(createImage(0, BLUE, helloApplication.getTitles()));
        card.setFitHeight(50);
        card.setFitWidth(50);
        card.setVisible(false);
        GridPane.setConstraints(card,8,8);
        mainBoardGrid.getChildren().add(card);
        card = new ImageView(createImage(0, BLUE, helloApplication.getTitles()));
        card.setFitHeight(50);
        card.setFitWidth(50);
        card.setVisible(false);
        GridPane.setConstraints(card,8,0);
        mainBoardGrid.getChildren().add(card);

        Card[][] mainBoard = player.getMainBoard().getBoard();
        for(int x=0;x<player.getMainBoard().getColumns();x++){
            for (int y=0;y<player.getMainBoard().getRows(); y++){
                    card = new ImageView(createImage(mainBoard[x][y].getSketch(), mainBoard[x][y].getColor(), helloApplication.getTitles()));
                    card.setFitHeight(50);
                    card.setFitWidth(50);
                    if(mainBoard[x][y].getColor().equals(EMPTY))card.setVisible(false);
                    card.setId("tile");
                    GridPane.setConstraints(card,x,player.getMainBoard().getColumns()-y-1);
                    mainBoardGrid.getChildren().add(card);
            }
        }

        livingRoomBox.getChildren().remove(node);
        livingRoomBox.getChildren().add(mainBoardGrid);
        livingRoomClickable.prefHeight(565);
        livingRoomClickable.prefWidth(565);
        livingRoomBox.getChildren().add(node);

    }

    /**
     * @param p is the parameter for the position
     */
    public void updateMainBoard(PositionWithColor[] p) {
        for (PositionWithColor pos : p) {
            for(Node n: mainBoardGrid.getChildren()){
                if(GridPane.getRowIndex(n) == player.getMainBoard().getColumns()-pos.getY()-1 && GridPane.getColumnIndex(n) == pos.getX())n.setVisible(false);
            }
        }
    }

    public void setMyPlayerBoardGrid(){
        myPlayerBoardGrid = new GridPane();
        myPlayerBoardGrid.setHgap(17.5);
        myPlayerBoardGrid.setVgap(9);
        myPlayerBoardGrid.setLayoutX(42);
        myPlayerBoardGrid.setLayoutY(-13);
        myPlayerBoardGrid.setId("myPlayerBoardGrid");
        PlayerBoard playerBoard = player.getPlayerBoard(player.getPlayerID());
        Card[][] cards = playerBoard.getBoard();

        for(int x=0;x<playerBoard.getColumns();x++){
            for (int y=0;y<playerBoard.getRows(); y++){
                ImageView card = new ImageView(createImage(cards[x][y].getSketch(), cards[x][y].getColor(), helloApplication.getTitles()));
                card.setFitHeight(42);
                card.setFitWidth(42);
                if(cards[x][y].getColor().equals(EMPTY))card.setVisible(false);
                GridPane.setConstraints(card, x, player.getMainBoard().getColumns()-y);
                myPlayerBoardGrid.getChildren().add(card);

            }
        }

        myBookshelfDiv.getChildren().add(myPlayerBoardGrid);
    }

    /**
     * @param id used to identify the player
     * @param column used to identify che column used to insert the chosen tails
     * @param c is the identifier of the card
     */
    public void updatePlayerBoard(String id, int column, Card[] c){
        if(id.equals(player.getPlayerID()))updatePlayerBoard(id,column,c,myPlayerBoardGrid,42);
        else updatePlayerBoard(id,column,c, otherPlayerBoardGrid.get(id), 17);
    }

    /**
     * @param playerID used to identify the player
     * @param column identifier of the column
     * @param c identify the ard
     * @param gridPane
     * @param size used to fit the cards on the gui
     */
    private void updatePlayerBoard(String playerID, int column, Card[] c, GridPane gridPane, int size){
        Card[][] playerBoard = player.getPlayerBoard(playerID).getBoard();
        Card[] actualColumn = playerBoard[column];
        int y = actualColumn.length-1;

        while (y>=0) {
            if (actualColumn[y].getColor() == EMPTY) y--;
            else break;
        }
        y = y -c.length +1;

        for(Card card: c){
            ImageView cardImg = new ImageView(createImage(card.getSketch(), card.getColor(), helloApplication.getTitles()));
            cardImg.setFitHeight(size);
            cardImg.setFitWidth(size);
            GridPane.setConstraints(cardImg, column, player.getMainBoard().getColumns()-y);
            gridPane.getChildren().add(cardImg);
            y++;
        }
    }

    public void setOtherPlayerBoard(){
        ArrayList<String> players = new ArrayList<>(List.of(player.getPlayersID()));
        players.remove(player.getPlayerID());

        otherPlayerBoardAnchors = new AnchorPane[player.getPlayersID().length-1];
        otherPlayerBoardAnchors[0] = bookshelfAnchor1;
        if(otherPlayerBoardAnchors.length>1) otherPlayerBoardAnchors[1] = bookshelfAnchor2;
        if(otherPlayerBoardAnchors.length>2) otherPlayerBoardAnchors[2] = bookshelfAnchor3;


        for(int i =0; i<players.size();i++){
            GridPane gridPane = new GridPane();
            gridPane.setVgap(3);
            gridPane.setHgap(5);
            gridPane.setLayoutX(17);
            gridPane.setLayoutY(-4);

            PlayerBoard playerBoard = player.getPlayerBoard(players.get(i));
            Card[][] cards = playerBoard.getBoard();

            for (int x = 0; x < playerBoard.getColumns(); x++) {
                for (int y = 0; y < playerBoard.getRows(); y++) {
                    ImageView card = new ImageView(createImage(cards[x][y].getSketch(),cards[x][y].getColor(), helloApplication.getTitles()));
                    card.setFitHeight(17);
                    card.setFitWidth(17);
                    if(cards[x][y].getColor().equals(EMPTY))card.setVisible(false);
                    GridPane.setConstraints(card, x, player.getMainBoard().getColumns() - y);
                    gridPane.getChildren().add(card);
                }
            }
            otherPlayerBoardGrid.put(players.get(i), gridPane);
            otherPlayerBoardAnchors[i].getChildren().add(gridPane);
        }
    }


    /**
     * @param x parameter used to estimate if the card selected is in a valid position
     * @param y parameter used to estimate if the card selected is in a valid position
     */
    @FXML
    public void takeCard(double x, double y){

        int columnX, lineY;
        columnX = Estimate.estimateX(x);
        lineY = Estimate.estimateY(y);

        for(Position p: positions){
            if(p.getX()==columnX && p.getY()==lineY){
                removeTakenCard(p.getX(),p.getY());
                return;
            }
        }

        Card[][] livingroom = player.getMainBoard().getBoard();
        CardColor card = livingroom[columnX][lineY].getColor();

        if(card.equals(EMPTY)){
            errorMsg("invalid pick");
            return;

        }

        if(positions.size() == 0) this.MoveMode();
        else if (positions.size() == maxTakenCard){
            this.errorMsg("can't take more pick");
            return;
        }
        positions.add(new Position(columnX, lineY));

        insertTails(columnX, lineY, positions.size()-1);

    }

    private void MoveMode(){
        othersBookshelf.addAll(bookshelfAnchor.getChildren());

        bookshelfAnchor.getChildren().clear();
        bookshelfAnchor.setAlignment(CENTER);
        VBox vBox = new VBox();
        vBox.setAlignment(CENTER);
        vBox.setSpacing(10);
        myTails = new HBox();
        myTails.setAlignment(CENTER);
        myTails.setId("myTails");
        HBox myTails2 = new HBox();
        myTails2.setSpacing(30);
        reorderMove = new TextField();
        reorderMove.setPromptText("new order");
        reorderMove.setId("reorderMove");
        columnMove = new TextField();
        columnMove.setId("columnMove");
        columnMove.setPromptText("column");
        Button sendMove = new Button();
        sendMove.setText("enter");
        sendMove.setId("sendMove");
        sendMove.setOnAction(event -> checkID(sendMove));
        Button resetMove = new Button();
        resetMove.setText("reset");
        resetMove.setId("resetMove");
        resetMove.setOnAction(event -> checkID(resetMove));
        Button changeOrder = new Button();
        changeOrder.setText("reorder");
        changeOrder.setId("changeOrder");
        changeOrder.setOnAction(event -> checkID(changeOrder));
        myTails2.getChildren().addAll(resetMove, reorderMove, changeOrder, columnMove, sendMove);

        vBox.getChildren().addAll(myTails, myTails2);
        bookshelfAnchor.getChildren().add(vBox);
    }

    /**
     * @param x parameter used to understand which tail we had to insert
     * @param y parameter used to understand which tail we had to insert
     */
    private void insertTails(int x, int y, int pose){

        Card[][] mainBoard = player.getMainBoard().getBoard();
        ImageView card = new ImageView(createImage(mainBoard[x][y].getSketch(), mainBoard[x][y].getColor(), helloApplication.getTitles()));
        card.setFitHeight(60);
        card.setFitWidth(60);
        card.setId("takenCard");
        card.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onClickReorde(pose);
            }
        });

        myTails.getChildren().add(card);

        for(Node n: mainBoardGrid.getChildren()){
            if(GridPane.getRowIndex(n) == player.getMainBoard().getColumns()-y-1 && GridPane.getColumnIndex(n) == x){
                n.setEffect(new ColorAdjust(0, -1, 0, 0));
                greyNode.add(n);
            }
        }
    }

    private void onClickReorde(int pose){
        Position tmp = positions.get(pose);
        positions.remove(pose);
        positions.add(0, tmp);

        myTails.getChildren().clear();
        this.insertAllTails();
    }

    private void insertAllTails(){
        for(int i=0;i< positions.size();i++){
            insertTails(positions.get(i).getX(), positions.get(i).getY(),i);
        }

    }


    private void sendMove(){

        if(!columnMove.getText().equals("")) {
            int column = 0;
            try {
                column = Integer.parseInt(columnMove.getText())-1;
            } catch (Exception e){
                errorMsg("invalid column value");
                return;
            }

            if(column >=0 && column <playerBoardRows  ){
                player.takeCard(column, positions.toArray(new Position[0]));
                this.resetMove();
            }else
                errorMsg("invalid column value");
        }
    }

    private void removeTakenCard(int x, int y){
        positions.removeIf(p -> p.getX() == x && p.getY() == y);
        for(Node n: mainBoardGrid.getChildren()){
            if(GridPane.getRowIndex(n) == player.getMainBoard().getColumns()-y-1 && GridPane.getColumnIndex(n) == x){
                n.setEffect(new ColorAdjust(0, 0, 0, 0));
            }
        }
        myTails.getChildren().clear();

        this.insertAllTails();
        if(positions.size()==0)resetMove();
    }
    private void resetMove(){
        bookshelfAnchor.getChildren().clear();
        bookshelfAnchor.getChildren().addAll(othersBookshelf);
        othersBookshelf = new ArrayList<>();
        positions = new ArrayList<>();
        resetGrey();
    }
    private void reorderMove(){
        String reorderText;
        ArrayList<Position> tmpPos = new ArrayList<>();
        int index,i;
        if(reorderMove != null ) {
            reorderText = reorderMove.getText();
            reorderText = reorderText.toLowerCase().replaceAll("\\s+", "");
            for (int j = 0; j < positions.size() - 1; j++) {
                index = reorderText.indexOf(',');
                if (index == -1) {
                    errorMsg("invalid syntax for reorder");
                }
                i=0;
                try {
                    i = Integer.parseInt(reorderText.substring(0,index))-1;
                } catch (Exception e){
                    errorMsg("invalid syntax for reorder");
                    return;
                }
                if(i<0||i>=positions.size()){
                    errorMsg("invalid index for reorder");
                    return;
                }

                tmpPos.add(positions.get(i));
                reorderText = reorderText.substring(index+1);
            }
            if(reorderText.indexOf(',')!=-1){
                errorMsg("invalid syntax for reorder");
            }
            i=0;
            try {
                i = Integer.parseInt(reorderText)-1;
            } catch (Exception e){
                errorMsg("invalid syntax for reorder");
                return;
            }
            if(i<0||i>=positions.size()){
                errorMsg("invalid index for reorder");
                return;
            }
            tmpPos.add(positions.get(i));
            positions = tmpPos;

            myTails.getChildren().clear();

            this.insertAllTails();
        }
    }



    private void resetGrey(){
        for(Node n: greyNode){
            n.setEffect(new ColorAdjust(0, 0, 0, 0));
        }
    }


    public void mouseCoordinates() {
        livingRoomClickable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            /**
             * @param event used to get coordinates when clicked on the mainboard
             */
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                GameController.this.takeCard(x,y);
            }
        });

        myPlayerBoardGrid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(positions.size()>0){
                    double x = event.getX();
                    if(x<43){
                        player.takeCard(0, positions.toArray(new Position[0]));
                        GameController.this.resetMove();
                    }
                    else if(x>61&&x<104){
                        player.takeCard(1, positions.toArray(new Position[0]));
                        GameController.this.resetMove();
                    }
                    else if(x>120&&x<163){
                        player.takeCard(2, positions.toArray(new Position[0]));
                        GameController.this.resetMove();
                    }
                    else if(x>179&&x<222){
                        player.takeCard(3, positions.toArray(new Position[0]));
                        GameController.this.resetMove();
                    }
                    else if(x>237){
                        player.takeCard(4, positions.toArray(new Position[0]));
                        GameController.this.resetMove();
                    }
                }
            }
        });
    }

    public void updateLastCommonGoal(){
        commonGoalsScore.setSpacing(50);
        commonGoalsScore.setTranslateY(15);
        commonGoalsScore.getChildren().clear();

        ArrayList<JsonObject> goal = new ArrayList<>(List.of(player.getCommonGoalScored()));
        goal.removeIf(jsonObject -> !jsonObject.get("playerID").getAsString().equals(player.getPlayerID()));
        for(JsonObject jsonObject: goal){
                if(goal.size() == 1 && jsonObject.get("commonGoalId").getAsInt()==1){
                    String imgFile = CommonGoalScore.getImgName(jsonObject.get("value").getAsInt());
                    Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(imgFile), 100, 100, false, false);
                    ImageView imgView = new ImageView(image);
                    imgView.setVisible(false);
                    commonGoalsScore.getChildren().add(imgView);
                }
                String imgFile = CommonGoalScore.getImgName(jsonObject.get("value").getAsInt());
                Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(imgFile), 100, 100, false, false);
                ImageView imgView = new ImageView(image);
                commonGoalsScore.getChildren().add(imgView);
        }
    }

    @FXML
    private void quitGame () { //quit button to quit the game and return to the create-game lobby
        this.musicStop();
        player.quitGame();
    }



    /**********************************************************************
     *                               CHAT                                 *
     **********************************************************************/

    private void chatInit(){
        chatName.getItems().add("ALL");
        for (String s: player.getPlayersID())if(!s.equals(player.getPlayerID()))chatName.getItems().add(s);
        chatName.setValue("ALL");
    }

    /**
     * @param msg is the message sent to the chat that the player need to see
     */
    public void chatReceiveMsg(Message msg){
        messages.add(msg);
        this.printMsg();
    }
    @FXML
    public void sendMSG(ActionEvent actionEvent) throws IOException {
        String msg = chatMSG.getText();
        if(msg.matches("")){
            this.errorMsg("fill the message text field before");
            return;
        }

        if(chatName.getValue().equals("ALL")){
            player.sendBroadcastMsg(msg);
            messages.add(new Message("You: "+ msg, MessageTipe.MINE));
        }else {
            try {
                player.sendPrivateMSG(chatName.getValue(), msg);
                messages.add(new Message("your: [PRIVATE] "+ msg, MessageTipe.MINE));
            } catch (PlayerNotFoundException e) {
                this.errorMsg("can't find player in this game");
            }
        }
        chatMSG.setText("");

        this.printMsg();
    }
    private void printMsg(){
        CornerRadii cornerRadii = new CornerRadii(9);
        VBox group = new VBox();
        group.setId("chatVbox");
        for(int i=0; i<=messages.size()-1;i++){
            Label label = new Label();
            label.setMaxWidth(320);
            label.setWrapText(true);
            label.setText(messages.get(i).getText());
            switch (messages.get(i).getMsgType()){
                case BROADCAST -> label.setBackground(new Background(new BackgroundFill(Color.rgb(0, 204, 153), cornerRadii, Insets.EMPTY)));
                case PRIVATE ->  label.setBackground(new Background(new BackgroundFill(Color.rgb(255, 153, 102), cornerRadii, Insets.EMPTY)));
                case MINE ->  label.setBackground(new Background(new BackgroundFill(Color.rgb(153, 204, 255), cornerRadii, Insets.EMPTY)));
                case ERROR -> label.setBackground(new Background(new BackgroundFill(Color.rgb(255, 102, 0), cornerRadii, Insets.EMPTY)));
            }
            group.getChildren().addAll(label);
        }
        messageContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageContainer.setContent(group);
    }

    /**
     * @param errorMsg is a string used in the pop-up when an error message is needed
     */
    @Override
    public void errorMsg(String errorMsg){
        if(chatOpen && !errorMsg.contains("CONNECTION ERROR")) {
            messages.add(new Message("[error] " + errorMsg, MessageTipe.ERROR));
            this.printMsg();
        }else {
            alert.setTitle("my shelfie");
            alert.setContentText(errorMsg);
            alert.show();
        }
    }

    /**********************************************************************
     *                               UX                                   *
     **********************************************************************
     */
    @FXML
    protected void hideChat(ActionEvent actionEvent) throws IOException {
        chatOpen = false;
        Node bookShelf = rightDiv.getChildren().get(0);
        Node chat = rightDiv.getChildren().get(1);

        chat.setVisible(false);

        HBox group = new HBox();
        Label label = new Label("show chat");
        group.setId("showChat");
        Button button = new Button("+");
        button.setId("showChatButton");
        button.setOnAction(event -> checkID(button));
        group.getChildren().addAll(label, button);

        rightDiv.getChildren().clear();
        rightDiv.getChildren().addAll(bookShelf, group, chat);
    }
    @FXML
    protected void hidePersonal(ActionEvent actionEvent) throws IOException {
        ArrayList<Node> nodes = new ArrayList<>();
        Node personal = null;
        for (Node node: leftDiv.getChildren()){
            if(node.getId().equals("personalGoals")){
                node.setVisible(false);
                personal = node;
            }
            else nodes.add(node);
        }



        HBox group = new HBox();
        Label label = new Label("show personal goal");
        group.setId("showPersonal");
        Button button = new Button("+");
        button.setId("showPersonalButton");
        button.setOnAction(event -> checkID(button));
        group.getChildren().addAll(label, button);

        leftDiv.getChildren().clear();

        leftDiv.getChildren().addAll(group);
        for (Node node: nodes)leftDiv.getChildren().add(node);
        leftDiv.getChildren().add(personal);
    }

    @FXML
    protected void hideCommon(ActionEvent actionEvent) throws IOException {
        ArrayList<Node> nodes = new ArrayList<>();
        Node common = null;
        boolean hidedPersonal = false;
        Node personal = null;
        for (Node node : leftDiv.getChildren()) {
            if(node.getId().equals("showPersonal")){
                hidedPersonal = true;
            }
            if (node.getId().equals("commonGoals")) {
                node.setVisible(false);
                common = node;
            } else if (node.getId().equals("personalGoals") && hidedPersonal) {
                personal = node;
            } else nodes.add(node);
        }

        HBox group = new HBox();
        Label label = new Label("show common goal");
        group.setId("showCommon");
        Button button = new Button("+");
        button.setId("showCommonButton");
        button.setOnAction(event -> checkID(button));
        group.getChildren().addAll(label, button);

        leftDiv.getChildren().clear();

        nodes.add(group);
        for (Node node: nodes)leftDiv.getChildren().add(node);
        if(hidedPersonal)leftDiv.getChildren().add(personal);
        leftDiv.getChildren().add(common);
    }

    @FXML
    protected void showPersonalButton(){
        ArrayList<Node> nodes = new ArrayList<>();
        Node personal = null;
        for (Node node: leftDiv.getChildren()){
            if(node.getId().equals("personalGoals")){
                node.setVisible(true);
                personal = node;
            }
            else if (node.getId().equals("showPersonal")){
                node.setVisible(false);
            }
            else nodes.add(node);
        }

        leftDiv.getChildren().clear();

        leftDiv.getChildren().add(personal);
        for (Node node: nodes)leftDiv.getChildren().add(node);
    }
    @FXML
    protected void showCommonButton(){
        ArrayList<Node> nodes = new ArrayList<>();
        Node  hidedPersonal = null;
        Node common = null;
        for (Node node: leftDiv.getChildren()){
            if(node.getId().equals("commonGoals")){
                node.setVisible(true);
                common = node;
            }
            else if (node.getId().equals("showCommon")){
                node.setVisible(false);
            }
            else if(node.getId().equals("showPersonal")){
                hidedPersonal = node;
            }
            else nodes.add(node);
        }

        leftDiv.getChildren().clear();

        if(hidedPersonal!=null) {
            leftDiv.getChildren().add(hidedPersonal);
            leftDiv.getChildren().add(common);
        }
        for (Node node: nodes)leftDiv.getChildren().add(node);
        if(hidedPersonal==null) leftDiv.getChildren().add(common);

    }
    @FXML
    protected void showChatButton(){
        chatOpen = true;
        Node bookShelf = rightDiv.getChildren().get(0);
        Node chat = rightDiv.getChildren().get(2);

        chat.setVisible(true);

        rightDiv.getChildren().clear();
        rightDiv.getChildren().addAll(bookShelf, chat);
    }
    @FXML
    protected void showMenu(ActionEvent actionEvent){
        CornerRadii cornerRadii = new CornerRadii(0);
        menu = new AnchorPane();
        menu.setPrefWidth(250);
        menu.setPrefHeight(1550);
        menu.setBackground(new Background(new BackgroundFill(Color.rgb(160,82,45), cornerRadii, Insets.EMPTY)));
        menu.getChildren().clear();

        Label label1 = new Label("MUSIC:");
        label1.setLayoutY(200);
        label1.setLayoutX(20);
        label1.setStyle("-fx-text-fill: white;");

        Button close = new Button("X");
        close.setBackground(new Background(new BackgroundFill(Color.rgb(160,82,45), cornerRadii, Insets.EMPTY)));
        close.setLayoutX(220);
        close.setId("closeMenu");
        close.setOnAction(event -> checkID(close));

        Button stopMusic = new Button("stop music");
        stopMusic.setId("stopMusic");
        stopMusic.setLayoutY(240);
        stopMusic.setLayoutX(70);
        stopMusic.setOnAction(event -> checkID(stopMusic));

        Button playMusic = new Button("play music");
        playMusic.setId("playMusic");
        playMusic.setLayoutY(300);
        playMusic.setLayoutX(70);
        playMusic.setOnAction(event -> checkID(playMusic));

        Button quitGame = new Button("quit game");
        quitGame.setId("quitGame");
        quitGame.setLayoutY(710);
        quitGame.setLayoutX(70);
        quitGame.setOnAction(event -> checkID(quitGame));

        menu.getChildren().addAll(close, label1, playMusic, stopMusic, quitGame);

        main.getChildren().add(menu);
    }
    private void hideMenu(){
        main.getChildren().remove(menu);
    }

    /**
     * @param button parameter that indicate the button clicked
     */
    private void checkID(Button button){
        switch (button.getId()) {
            case "showChatButton" -> this.showChatButton();
            case "showPersonalButton" -> this.showPersonalButton();
            case "showCommonButton" -> this.showCommonButton();
            case "resetMove" -> this.resetMove();
            case "sendMove" -> this.sendMove();
            case "changeOrder" -> this.reorderMove();
            case "quitGame" -> this.quitGame();
            case "playMusic"  -> this.musicStart();
            case "stopMusic" -> this.musicStop();
            case "closeMenu" -> this.hideMenu();
        }
    }



    /**********************************************************************
     *                               music                                *
     **********************************************************************
     */
    private void musicInit(){
        Media music = new Media(this.getClass().getClassLoader().getResource("background.wav").toExternalForm());
        backgroundMusic = new MediaPlayer(music);
        backgroundMusic.setVolume(0.1);
        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
    }
    private void musicStart(){
        backgroundMusic.play();
    }
    public void musicStop(){
        backgroundMusic.stop();
    }
}
