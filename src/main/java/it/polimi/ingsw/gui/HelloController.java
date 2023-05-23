package it.polimi.ingsw.gui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.ClientRMI;
import it.polimi.ingsw.client.Connection.ClientSOCKET;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
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
    protected void rmiClick(ActionEvent actionEvent) throws IOException {
        this.jsonCreate();
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
        this.jsonCreate();
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


}
