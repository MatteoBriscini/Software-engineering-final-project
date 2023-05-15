package it.polimi.ingsw.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;

public class HelloController extends GuiView{

@FXML
private Label passwordLabel;

@FXML
private Label usernameLabel;

@FXML
private Label errorLabel;
private AnchorPane loginPanel;





    /* @FXML
    protected void onHelloButtonClick() {
        this.errorLabel.setText("");
        if(!this.usernameLabel.getText().matches("example")) this.errorLabel.setText("password or username incorrect");
        else this.errorLabel.setText("welcome");
        if(!this.passwordLabel.getText().matches("password")) this.errorLabel.setText("password or username incorrect");
        else{
            this.errorLabel.setText("welcome");
            //to do... new scene
        } */

    @FXML
    protected void rmiClick(ActionEvent actionEvent) throws IOException {


            helloApplication.changeView("login.fxml");


    }


    @FXML
    protected void socketClick(ActionEvent actionEvent) throws IOException {


            helloApplication.changeView("login.fxml");

    }


}
