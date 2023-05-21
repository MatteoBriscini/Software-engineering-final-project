package it.polimi.ingsw.gui;

import it.polimi.ingsw.gui.supportClass.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController extends GuiView implements Initializable {


    private PlayingPlayer player;

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
    //ux
    @FXML
    private VBox rightDiv = new VBox();
    @FXML
    private VBox leftDiv = new VBox();
    private ArrayList<Message> messages =new ArrayList<>();
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {            //static element init
        imagesInit(myBookshelfImage, "bookshelf.png"); //load player board img
        imagesInit(myBookshelfImage1, "bookshelf.png");
        imagesInit(myBookshelfImage2, "bookshelf.png");
        imagesInit(myBookshelfImage3, "bookshelf.png");

        imagesInit(livingRoom, "livingroom.png");       //load living room png
        this.setBorderRadius(livingRoom, 40);

        imagesInit(commonGoal1, "1.jpg");               //load common goal img TODO
        this.setBorderRadius(commonGoal1, 40);
        imagesInit(commonGoal2, "2.jpg");
        this.setBorderRadius(commonGoal2, 40);

        imagesInit(personalGoal1, "Personal_Goals.png");//load private goal img TODO
        this.setBorderRadius(personalGoal1, 40);
    }
    public void gameInit(){                         //dynamic element init
        player = (PlayingPlayer) helloApplication.getPlayer();
        chatInit();
    }
    private void imagesInit(ImageView imageView, String file){
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(file));
        imageView.setImage(image);
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
        System.out.println(messageContainer.getContent());
        System.out.println("ciao");
            messages.add(msg);
            VBox group = new VBox();
            for(int i=messages.size(); i>=0; i--){
                Label label = new Label();
                label.setText(msg.getText());
                group.getChildren().addAll(label);
            }
            messageContainer.setContent(group);
        System.out.println(group.getChildren());
        System.out.println(messages);
    }

    /**********************************************************************
     *                               UX                                   *
     **********************************************************************
     * @param actionEvent
     * @throws IOException
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
        switch (button.getId()){
            case "showChatButton":
                this.showChatButton();
                break;
            case "showPersonalButton":
                this.showPersonalButton();
                break;
            case "showCommonButton":
                this.showCommonButton();
                break;
        }
    }

}
