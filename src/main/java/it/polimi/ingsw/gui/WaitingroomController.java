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
public class WaitingroomController extends GuiView{

    @FXML
    private Label playerGamesLabel = new Label();

    private void printNplayers (){
        System.out.println();
    }

    @FXML
    protected void startClick(ActionEvent actionEvent){

        Player player = helloApplication.getPlayer();

        ((PlayingPlayer)player).startGame();

    }

    public void changeNumPlayer(int num){

        System.out.println(num);
        playerGamesLabel.setText("hello world");
    }

}
