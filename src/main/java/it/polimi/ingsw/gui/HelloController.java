package it.polimi.ingsw.gui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.ClientRMI;
import it.polimi.ingsw.client.Connection.ClientSOCKET;
import it.polimi.ingsw.gui.supportClass.Title;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HelloController extends GuiView implements Initializable {

    @FXML
    private Label passwordLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label errorLabel;
    private AnchorPane loginPanel;
    @FXML
    protected VBox main;
    private VBox settingsbox;
    boolean openSettings = false;
    private TextField IP;



    int socketPort;
    int RMIPort;
    String serverIP;
    Alert cntAlert = new Alert(Alert.AlertType.ERROR);


    private void jsonCreate() throws FileNotFoundException{
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("netConfig"));
        if(inputStream == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject  jsonObject = new Gson().fromJson(bufferedReader, JsonObject.class);
        this.serverIP = jsonObject.get("serverIP").getAsString();
        this.socketPort = jsonObject.get("defSocketPort").getAsInt();
        this.RMIPort = jsonObject.get("defRmiPort").getAsInt();
    }
    @FXML
    protected void settings(ActionEvent actionEvent) throws IOException {
        if(!openSettings){
            openSettings = true;

            settingsbox = new VBox();
            settingsbox.setAlignment(Pos.CENTER);
            settingsbox.setSpacing(20);
            settingsbox.setStyle("" +
                    "-fx-background-color: rgb(160,82,45);" +
                    "-fx-background-radius: 20px;" +
                    "-fx-padding: 15px;"+
                    "-fx-max-width: 290px");
            IP = new TextField();
            IP.setId("serverIP");
            IP.setPromptText("server ip");
            Button button = new Button("apply");
            button.setId("applyBt");
            button.setOnAction(event -> checkID(button));
            settingsbox.getChildren().addAll(IP, button);
            main.getChildren().add(settingsbox);
        } else {
            openSettings = false;
            main.getChildren().remove(settingsbox);
        }
    }
    @FXML
    protected void applySettings(){
        if(!IP.getText().matches("")) {
            serverIP = IP.getText();
           openSettings = false;
           main.getChildren().remove(settingsbox);
        }
    }
    @FXML
    protected void rmiClick(ActionEvent actionEvent) throws IOException {
        this.buttonClickedAudio();

        try {
            helloApplication.setConnection(new ClientRMI(RMIPort, serverIP));
        } catch (Exception e) {
            cntAlert.setTitle("CONNECTION ERROR");
            cntAlert.setContentText("apparently the server is offline");
            cntAlert.show();

            return;
        }
        helloApplication.changeView("login.fxml");
    }


    @FXML
    protected void socketClick(ActionEvent actionEvent) throws IOException {
        this.buttonClickedAudio();

        try {
            helloApplication.setConnection(new ClientSOCKET(socketPort, serverIP));
        } catch (Exception e) {
            alert.setTitle("connection error");
            alert.setContentText("apparently the server is offline");
            alert.show();
            return;
        }
        helloApplication.changeView("login.fxml");

    }


    private void checkID(Button button){
        switch (button.getId()) {
            case "applyBt" -> this.applySettings();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.jsonCreate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
