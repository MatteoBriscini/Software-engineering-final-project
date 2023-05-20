package it.polimi.ingsw.gui;

import it.polimi.ingsw.client.Connection.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import it.polimi.ingsw.client.Player.LobbyPlayer;
import it.polimi.ingsw.client.Player.Player;
import javafx.scene.control.TextField;
import java.io.*;

public class RegistrationController extends GuiView{

    private Player player;
    private ConnectionManager connection;
    @FXML
    private TextField userIDtext = new TextField();
    @FXML
    private TextField passText = new TextField();

    @FXML
    protected void enterButton(ActionEvent actionEvent) throws IOException {

        // connection = getConnection();

        if(!this.userIDtext.getText().matches("") && !this.passText.getText().matches("")){
            String userID;
            String password;
            boolean logged;

            userID = userIDtext.getText();
            System.out.println(userID);
            password = passText.getText();
            System.out.println(password);
            player = new LobbyPlayer(userID, password, helloApplication.getConnection());
            player.setUi(helloApplication);
            logged = ((LobbyPlayer) player).signUp();

            if (logged) {
                helloApplication.changeView("creategame.fxml");
                helloApplication.setPlayer(player);
            }
        }else
          this.errorMsg("username or password text field empty");

    }


}
