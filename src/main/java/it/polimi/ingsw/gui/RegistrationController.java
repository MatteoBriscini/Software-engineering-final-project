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

public class RegistrationController extends GuiView{

    private Player player;
    private ConnectionManager connection;
    @FXML
    private TextField userIDtext;
    @FXML
    private TextField passText;

    protected void enterClick() throws IOException {

        // connection = getConnection();

       /* String userID;
        String password;
        boolean logged;

        userID =  userIDtext.getText();
        password = passText.getText();
        player = new LobbyPlayer(userID,password, helloApplication.getConnection());
        logged = ((LobbyPlayer) player).signUp();

        if(logged)
          helloApplication.changeView("creategame.fxml"); */

    }


}
