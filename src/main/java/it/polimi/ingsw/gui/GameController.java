package it.polimi.ingsw.gui;

import it.polimi.ingsw.client.Connection.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
    public void initialize(URL url, ResourceBundle resourceBundle) {

       // player = (PlayingPlayer) helloApplication.getPlayer();

        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream("bookshelf.png"));
        myBookshelfImage.setImage(image);

        myBookshelfImage1.setImage(image);

        myBookshelfImage2.setImage(image);

        myBookshelfImage3.setImage(image);  //load the other boards

        Image image1 = new Image(this.getClass().getClassLoader().getResourceAsStream("livingroom.png"));
        livingRoom.setImage(image1);

        Image image2 = new Image(this.getClass().getClassLoader().getResourceAsStream("1.jpg"));
        commonGoal1.setImage(image2);

        Image image3 = new Image(this.getClass().getClassLoader().getResourceAsStream("2.jpg"));
        commonGoal2.setImage(image3);




        Image image4 = new Image(this.getClass().getClassLoader().getResourceAsStream("Personal_goals.png"));
        personalGoal1.setImage(image4);


    }

}
