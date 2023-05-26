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

public class CreategameController extends GuiView{

    @FXML
    private TextField maxNumberTextfield;

    @FXML
    private TextField gameIDTextfield;
    private int minPlayers;
    private int maxPlayers;

    private void jsonCreate() throws FileNotFoundException{

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("controllerConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = new Gson().fromJson(bufferedReader, JsonObject.class);
        this.minPlayers = jsonObject.get("minPlayerNumber").getAsInt();
        this.maxPlayers = jsonObject.get("maxPlayerNumber").getAsInt();
    }

    @FXML
    protected void createCustom(ActionEvent actionEvent) throws IOException {
        this.buttonClickedAudio();
        this.jsonCreate();
        int i;
        i = Integer.parseInt(String.valueOf(this.maxNumberTextfield.getText()));

        if(!this.maxNumberTextfield.getText().matches("") && i>=minPlayers && i<= maxPlayers) {

            int x = Integer.parseInt(maxNumberTextfield.getText());
            Player player = helloApplication.getPlayer();

            if (((LobbyPlayer) player).createGame(x))
                helloApplication.changeView("waitingroom.fxml");
        }else
            this.errorMsg("players number incorrect, please put a number between 2-4");
    }

    @FXML
    protected void joinCustom(ActionEvent actionEvent) throws IOException {
        this.buttonClickedAudio();
        if(!this.gameIDTextfield.getText().matches("")) {
            String x = gameIDTextfield.getText();
            Player player = helloApplication.getPlayer();

            if (((LobbyPlayer) player).joinGame(x))
                helloApplication.changeView("waitingroom.fxml");
        }else
            this.errorMsg("invalid GameID");
    }

    @FXML
    protected void createDefault(ActionEvent actionEvent) throws IOException {
        this.buttonClickedAudio();
        Player player = helloApplication.getPlayer();

        if(((LobbyPlayer)player).createGame())
            helloApplication.changeView("waitingroom.fxml");


    }

    @FXML
    protected  void joinDefault(ActionEvent actionEvent) throws IOException {
        this.buttonClickedAudio();
        Player player = helloApplication.getPlayer();

        if(((LobbyPlayer)player).joinGame())
            helloApplication.changeView("waitingroom.fxml");
    }


}
