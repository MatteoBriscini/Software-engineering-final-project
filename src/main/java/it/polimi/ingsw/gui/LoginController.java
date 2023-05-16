package it.polimi.ingsw.gui;

import it.polimi.ingsw.client.Connection.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.Stage;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class LoginController extends GuiView {
    private Player player;

    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField userIDTextField;

    @FXML
    private Label regPass;
    @FXML
    private TextField errP;


    public void registerButton(ActionEvent actionEvent) throws IOException {

          /*  HelloController connection = new HelloController();
            Object connection1 = connection.getConnection(); */

            helloApplication.changeView("registration.fxml");
        }



     public void loginButton(ActionEvent actionEvent) throws IOException {
        //check password and username, else error "password or username incorrect"

        /* String userID;
         String password;
         boolean logged;

         userID =  userIDTextField.getText();
         password = passwordTextField.getText();
         player = new LobbyPlayer(userID,password, helloApplication.getConnection());
         logged = ((LobbyPlayer) player).login();
         //controlli su texfields vuoti e loop se login non a buon fine


        if(logged)
            helloApplication.changeView("creategame.fxml"); */

    }
}
