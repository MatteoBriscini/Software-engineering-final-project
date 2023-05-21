package it.polimi.ingsw.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import it.polimi.ingsw.client.Player.LobbyPlayer;
import it.polimi.ingsw.client.Player.Player;
import javafx.scene.control.TextField;
import java.io.*;


public class LoginController extends GuiView {
    private Player player;

    @FXML
    private TextField passwordTestField = new TextField();
    @FXML
    private TextField userIDTextField = new TextField();

    @FXML
    private Label regPass;
    @FXML
    private TextField errP;


    @FXML
    protected void registerButton(ActionEvent actionEvent) throws IOException {
            helloApplication.changeView("registration.fxml");
        }


        @FXML
     protected void loginButton(ActionEvent actionEvent) throws IOException {
        //check password and username, else error "password or username incorrect"
            if(!this.userIDTextField.getText().matches("") && !this.passwordTestField.getText().matches("")) {
                String userID;
                String password;
                boolean logged;

                userID = userIDTextField.getText();
                password = passwordTestField.getText();
                player = new LobbyPlayer(userID, password, helloApplication.getConnection());
                player.setUi(helloApplication);
                logged = ((LobbyPlayer) player).login();


                if (logged) {
                    helloApplication.setPlayer(player);
                    helloApplication.changeView("creategame.fxml");
                }
            }else
             this.errorMsg("password or userID textField empty");
    }
}
