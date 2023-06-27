package it.polimi.ingsw.gui;

import com.google.gson.JsonObject;
import it.polimi.ingsw.shared.PlayerMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import javafx.scene.image.ImageView;

import javafx.scene.layout.*;


import java.net.URL;
import java.util.*;



public class EndgameController extends GuiView implements Initializable{

    public AnchorPane podium;
    @FXML
    private ImageView podiumImage = new ImageView();
    @FXML
    private ImageView logoImage = new ImageView();
    @FXML
    private VBox placing = new VBox();
    @FXML
    public Label p1 = new Label();
    @FXML
    public Label p2 = new Label();
    @FXML
    public Label p3 = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imagesInit(podiumImage, "podium.png"); //load player board img
        imagesInit(logoImage, "Title.png");
    }

    /**
     * print the final classification
     */
    public void endgamePoints(JsonObject points){

        PlayingPlayer player = (PlayingPlayer) helloApplication.getPlayer();
        int n=((PlayingPlayer)player).getPlayersID().length;
        String[][] table = new String[n][n];
        for(int i=0;i<n; i++){
            table[i][0]= ((PlayingPlayer)player).getPlayersID()[i];
            table[i][1]= points.get(((PlayingPlayer)player).getPlayersID()[i]).getAsString();
        }
        Arrays.sort(table, Comparator.comparingInt(row->Integer.parseInt(row[1])));

        for(int i=n-1;i>=0;i--){
            Label label = new Label();
            label.setText(table[i][0]+ ": " + table[i][1]);
            if (table[i][0].equals(player.getPlayerID())) label.setId("yourScore");
            placing.getChildren().add(label);
        }

        p1.setText((String) table[table.length-1][0]);
        p2.setText((String) table[table.length-2][0]);

        if(table.length > 2) {
            p3.setText((String) table[table.length-3][0]);
        }else podium.getChildren().remove(p3);

    }
    /**
     * change scene back to select game scene
     */
    @FXML
    protected void backToLobby(ActionEvent actionEvent){
        helloApplication.getConnection().setPlayerAsLobby();
        helloApplication.setMode(PlayerMode.LOBBY);
    }

}
