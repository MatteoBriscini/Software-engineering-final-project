package it.polimi.ingsw.gui;


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

public class CreategameController extends GuiView{

    @FXML
    private TextField playerNumber;

    @FXML
    private TextField gameID;
    @FXML
    private Label wrongID;
    @FXML
    private Label excededN;
    int x=0;


    public void creategame(ActionEvent actionEvent){

        if(!playerNumber.getText().matches("2|3|4")){
            this.excededN.setText("please, select a number between 2-4");
            x = x+1;
            if(x>2){
                this.excededN.setText("bruh, you dumb?");
            }
        }else{
            //create on backand a game with that player limit and put the current player in
        }

    }

    public void joingame(ActionEvent actionEvent){
        if(1==1){
            //do a check on the gamesID, else write an error label
        }else{
            this.wrongID.setText("wrong ID selected");
        }

    }


}
