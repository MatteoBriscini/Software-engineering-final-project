package it.polimi.ingsw.gui;

import it.polimi.ingsw.client.Connection.ConnectionManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Player.LobbyPlayer;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.exceptions.addPlayerToGameException;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController extends GuiView implements Initializable {


    PlayingPlayer player;

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
    private VBox rightDiv = new VBox();
    @FXML
    private VBox leftDiv = new VBox();
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

       // player = (PlayingPlayer) helloApplication.getPlayer();

        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream("bookshelf.png"));
        myBookshelfImage.setImage(image);

        myBookshelfImage1.setImage(image);

        myBookshelfImage2.setImage(image);

        myBookshelfImage3.setImage(image);  //load the other boards

        Image image1 = new Image(this.getClass().getClassLoader().getResourceAsStream("livingroom.png"));
        livingRoom.setImage(image1);
        this.setBorderRadius(livingRoom, 40);

        Image image2 = new Image(this.getClass().getClassLoader().getResourceAsStream("1.jpg"));
        commonGoal1.setImage(image2);
        this.setBorderRadius(commonGoal1, 40);

        Image image3 = new Image(this.getClass().getClassLoader().getResourceAsStream("2.jpg"));
        commonGoal2.setImage(image3);
        this.setBorderRadius(commonGoal2, 40);

        Image image4 = new Image(this.getClass().getClassLoader().getResourceAsStream("Personal_Goals.png"));
        personalGoal1.setImage(image4);
        this.setBorderRadius(personalGoal1, 40);

    }

    private void setBorderRadius (ImageView immage, int size){
        Rectangle clip = new Rectangle(
                immage.getFitWidth(), immage.getFitHeight()
        );
        clip.setArcWidth(size);
        clip.setArcHeight(size);
        immage.setClip(clip);
    }

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
