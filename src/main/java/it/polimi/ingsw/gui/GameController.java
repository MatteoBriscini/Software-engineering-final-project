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
import javafx.scene.Node;
import javafx.scene.control.*;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

    private GridPane[] otherPlayerBoardGrid;
    private ImageView[] otherPlayerBoardBox;
    @FXML
    private VBox myBookshelfBox1;
    @FXML
    private VBox myBookshelfBox2;
    @FXML
    private VBox myBookshelfBox3;
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

    //chat
    @FXML
    private ChoiceBox<String> chatName = new ChoiceBox<>();
    @FXML
    private ScrollPane messageContainer = new ScrollPane();
    @FXML
    private TextField chatMSG = new TextField();
    private ArrayList<Message> messages =new ArrayList<>();

    //ux
    @FXML
    private VBox rightDiv = new VBox();
    @FXML
    private VBox leftDiv = new VBox();


    /**********************************************************************
     *                              initialize                            *
     **********************************************************************/
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {            //static element init
        imagesInit(myBookshelfImage, "bookshelf_orth.png"); //load player board img

        imagesInit(logo, "Publisher.png");

        imagesInit(livingRoom, "livingroom.png");       //load living room png
        this.setBorderRadius(livingRoom, 40);
    }


    public void gameInit(){                         //dynamic element init
        player = (PlayingPlayer) helloApplication.getPlayer();
        chatInit();
        playerInit();
        commonGoalsInit();
        privateGoalsInit();
        setMainBoard();
        setMyPlayerBoardGrid();

        this.mouseCoordinates();
        this.notifyNewActivePlayer();
    }
    private void imagesInit(ImageView imageView, String file){
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(file));
        imageView.setImage(image);
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

        imagesInit(myBookshelfImage2, "bookshelf.png");
        imagesInit(myBookshelfImage3, "bookshelf.png");


        if(otherPlayers.size()>1) {
            imagesInit(myBookshelfImage2, "bookshelf_orth.png");
            shelfID2.setText(otherPlayers.get(1));
        }
        if(otherPlayers.size()>2) {
            imagesInit(myBookshelfImage3, "bookshelf_orth.png");
            shelfID2.setText(otherPlayers.get(2));
        }
    }
    private void setBorderRadius (ImageView immage, int size){
        Rectangle clip = new Rectangle(
                immage.getFitWidth(), immage.getFitHeight()
        );
        clip.setArcWidth(size);
        clip.setArcHeight(size);
        immage.setClip(clip);
    }

    /**********************************************************************
     *                               GAME                                 *
     **********************************************************************/

    public void notifyNewActivePlayer() {
        String currentPlayer = player.getActivePlayer();
        this.currentPlayer.setText("CURRENT PLAYER: \n" + currentPlayer);
    }
    private void setMainBoard(){

        Node node = livingRoomBox.getChildren().get(2);
        mainBoardGrid = new GridPane();
        mainBoardGrid.setVgap(7);
        mainBoardGrid.setHgap(7);
        mainBoardGrid.setLayoutX(32);
        mainBoardGrid.setLayoutY(28);
        mainBoardGrid.setId("cardGrid");

        //TO FIX BOARD DIMENSION
        String file = CardImage.getImgName(0,BLUE);
        ImageView card = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(file)));
        card.setFitHeight(50);
        card.setFitWidth(50);
        card.setVisible(false);
        GridPane.setConstraints(card,0,0);
        mainBoardGrid.getChildren().add(card);
        card = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(file)));
        card.setFitHeight(50);
        card.setFitWidth(50);
        card.setVisible(false);
        GridPane.setConstraints(card,0,9);
        mainBoardGrid.getChildren().add(card);
        card = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(file)));
        card.setFitHeight(50);
        card.setFitWidth(50);
        card.setVisible(false);
        GridPane.setConstraints(card,8,9);
        mainBoardGrid.getChildren().add(card);
        card = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(file)));
        card.setFitHeight(50);
        card.setFitWidth(50);
        card.setVisible(false);
        GridPane.setConstraints(card,8,0);
        mainBoardGrid.getChildren().add(card);

        Card[][] mainBoard = player.getMainBoard().getBoard();
        for(int x=0;x<player.getMainBoard().getColumns();x++){
            for (int y=0;y<player.getMainBoard().getRows(); y++){
                if(!mainBoard[x][y].getColor().equals(EMPTY)){
                    file = CardImage.getImgName(mainBoard[x][y].getSketch(), mainBoard[x][y].getColor());
                    card = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(file)));
                    card.setFitHeight(50);
                    card.setFitWidth(50);
                    card.setId("tile");
                    GridPane.setConstraints(card,x,player.getMainBoard().getColumns()-y);
                    mainBoardGrid.getChildren().add(card);
                }
            }
        }

        livingRoomBox.getChildren().remove(node);
        livingRoomBox.getChildren().add(mainBoardGrid);
        livingRoomClickable.prefHeight(565);
        livingRoomClickable.prefWidth(565);
        livingRoomBox.getChildren().add(node);

    }
    public void updateMainBoard(PositionWithColor[] p) {
        for (PositionWithColor pos : p) {
            for(Node n: mainBoardGrid.getChildren()){
                 if(GridPane.getRowIndex(n) == player.getMainBoard().getColumns()-pos.getY() && GridPane.getColumnIndex(n) == pos.getX())n.setVisible(false);
            }
        }
    }

    public void setMyPlayerBoardGrid(){
        myPlayerBoardGrid = new GridPane();
        myPlayerBoardGrid.setHgap(17.5);
        myPlayerBoardGrid.setVgap(9);
        myPlayerBoardGrid.setLayoutX(42);
        myPlayerBoardGrid.setLayoutY(-13);
        PlayerBoard playerBoard = player.getPlayerBoard(player.getPlayerID());
        Card[][] cards = playerBoard.getBoard();

        for(int x=0;x<playerBoard.getColumns();x++){
            for (int y=0;y<playerBoard.getRows(); y++){
                    String file = CardImage.getImgName(cards[x][y].getSketch(), cards[x][y].getColor());
                    ImageView card = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(file)));
                    card.setFitHeight(42);
                    card.setFitWidth(42);
                    if(cards[x][y].getColor().equals(EMPTY))card.setVisible(false);
                    GridPane.setConstraints(card, x, player.getMainBoard().getColumns()-y);
                    myPlayerBoardGrid.getChildren().add(card);

            }
        }

        myBookshelfDiv.getChildren().add(myPlayerBoardGrid);
    }
    public void updatePlayerBoard(String id, int column, Card[] c){
        if(id.equals(player.getPlayerID()))updateMyPlayerBoard(column,c);
    }
    private void updateMyPlayerBoard(int column, Card[] c){
        Card[][] playerBoard = player.getPlayerBoard(player.getPlayerID()).getBoard();
        Card[] actualColumn = playerBoard[column];
        int y = actualColumn.length-1;

        while (y>=0) {
            if (actualColumn[y].getColor() == EMPTY) y--;
            else break;
        }
        y = y -c.length +1;

        for(Card card: c){
                    String file = CardImage.getImgName(card.getSketch(), card.getColor());
                    ImageView cardImg = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(file)));
                    cardImg.setFitHeight(42);
                    cardImg.setFitWidth(42);
                    GridPane.setConstraints(cardImg, column, player.getMainBoard().getColumns()-y);
                    myPlayerBoardGrid.getChildren().add(cardImg);
                    y++;
        }
    }

    public void setOtherPlayerBoard(){
        otherPlayerBoardGrid = new GridPane[player.getPlayersNumber()];

    }

    @FXML
    public void takeCard(double x, double y){

        int columnX, lineY;
        columnX = Estimate.estimateX(x);
        lineY = Estimate.estimateY(y);

        Card[][] livingroom = player.getMainBoard().getBoard();
        CardColor card = livingroom[columnX][lineY].getColor();

        if(card.equals(EMPTY)){
            errorMsg("invalid pick");
            return;

        }

        if(positions.size() == 0) this.MoveMode();
        else if (positions.size() == 3){                    //TODO dynamic number
            this.errorMsg("can't take more pick");
            return;
        }
        positions.add(new Position(columnX, lineY));

        insertTails(columnX, lineY);

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

    private void insertTails(int x, int y){

        Card[][] mainBoard = player.getMainBoard().getBoard();

        String file = CardImage.getImgName(mainBoard[x][y].getSketch(), mainBoard[x][y].getColor());
        ImageView card = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(file)));
        card.setFitHeight(60);
        card.setFitWidth(60);

        myTails.getChildren().add(card);

        for(Node n: mainBoardGrid.getChildren()){
            if(GridPane.getRowIndex(n) == player.getMainBoard().getColumns()-y && GridPane.getColumnIndex(n) == x)n.setEffect(new ColorAdjust(0, -1, 0, 0));
        }
    }

    private void sendMove(){
        //TODO controlli su input field
        int column = Integer.parseInt(columnMove.getText());
        player.takeCard(column, positions.toArray(new Position[0]));
        this.resetMove();
    }
    private void resetMove(){
        bookshelfAnchor.getChildren().clear();
        bookshelfAnchor.getChildren().addAll(othersBookshelf);
        othersBookshelf = new ArrayList<>();
        positions = new ArrayList<>();
    }
    private void reorderMove(){
        //TODO
    }



    public void mouseCoordinates() {
        livingRoomClickable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                // System.out.println("Mouse clicked at (" + x + ", " + y + ")");
                GameController.this.takeCard(x,y);
            }
        });
    }



    /**********************************************************************
     *                               CHAT                                 *
     **********************************************************************/

    private void chatInit(){
        chatName.getItems().add("ALL");
        for (String s: player.getPlayersID())if(!s.equals(player.getPlayerID()))chatName.getItems().add(s);
        chatName.setValue("ALL");
    }

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
            }
            group.getChildren().addAll(label);
        }
        messageContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageContainer.setContent(group);
    }

    /**********************************************************************
     *                               UX                                   *
     **********************************************************************
     */
    @FXML
    protected void hideChat(ActionEvent actionEvent) throws IOException {
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
        Node bookShelf = rightDiv.getChildren().get(0);
        Node chat = rightDiv.getChildren().get(2);

        chat.setVisible(true);

        rightDiv.getChildren().clear();
        rightDiv.getChildren().addAll(bookShelf, chat);
    }


    private void checkID(Button button){
        switch (button.getId()) {
            case "showChatButton" -> this.showChatButton();
            case "showPersonalButton" -> this.showPersonalButton();
            case "showCommonButton" -> this.showCommonButton();
            case "resetMove" -> this.resetMove();
            case "sendMove" -> this.sendMove();
            case "changeOrder" -> this.reorderMove();
        }
    }

}
