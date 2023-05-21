package it.polimi.ingsw.gui;

import it.polimi.ingsw.client.Exceptions.PlayerNotFoundException;
import it.polimi.ingsw.gui.supportClass.Message;
import it.polimi.ingsw.gui.supportClass.MessageTipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController extends GuiView implements Initializable {


    //game
    private PlayingPlayer player;
    private ArrayList<String> otherPlayers = new ArrayList<>();

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
        imagesInit(myBookshelfImage, "bookshelf.png"); //load player board img

        imagesInit(logo, "Publisher.png");

        imagesInit(livingRoom, "livingroom.png");       //load living room png
        this.setBorderRadius(livingRoom, 40);

        imagesInit(personalGoal1, "Personal_Goals.png");//load private goal img TODO
        this.setBorderRadius(personalGoal1, 40);
    }
    public void gameInit(){                         //dynamic element init
        player = (PlayingPlayer) helloApplication.getPlayer();
        chatInit();
        playerInit();
        commonGoalsInit();
    }
    private void imagesInit(ImageView imageView, String file){
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(file));
        imageView.setImage(image);
    }

    private void commonGoalsInit(){
        int[] goals = player.getCommonGoalID();
        ImageView[] commonGoal = new ImageView[2];
        commonGoal[0] = commonGoal1; commonGoal[1] = commonGoal2;
        for(int i=0;i<goals.length;i++){switch (goals[i]){
            case 0 -> imagesInit(commonGoal[i], "4.jpg");
            case 1 -> imagesInit(commonGoal[i], "3.jpg");
            case 2 -> imagesInit(commonGoal[i], "8.jpg");
            case 3 -> imagesInit(commonGoal[i], "11.jpg");
            case 4 -> imagesInit(commonGoal[i], "10.jpg");
            case 5 -> imagesInit(commonGoal[i], "2.jpg");
            case 6 -> imagesInit(commonGoal[i], "6.jpg");
            case 7 -> imagesInit(commonGoal[i], "5.jpg");
            case 8 -> imagesInit(commonGoal[i], "7.jpg");
            case 9 -> imagesInit(commonGoal[i], "9.jpg");
            case 10 -> imagesInit(commonGoal[i], "1.jpg");
            case 11 -> imagesInit(commonGoal[i], "12.jpg");
            }
        this.setBorderRadius(commonGoal[i], 40);
        }
    }

    private void playerInit(){
        for(String s: player.getPlayersID())if(!s.equals(player.getPlayerID()))otherPlayers.add(s);
        imagesInit(myBookshelfImage1, "bookshelf.png");
        shelfID1.setText(otherPlayers.get(0));

        imagesInit(myBookshelfImage2, "bookshelf_orth.png");
        imagesInit(myBookshelfImage3, "bookshelf_orth.png");


        if(otherPlayers.size()>1) {
            imagesInit(myBookshelfImage2, "bookshelf.png");
            shelfID2.setText(otherPlayers.get(1));
        }
        if(otherPlayers.size()>2) {
            imagesInit(myBookshelfImage3, "bookshelf.png");
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
            messages.add(new Message("your: "+ msg, MessageTipe.MINE));
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
        }
    }

}
