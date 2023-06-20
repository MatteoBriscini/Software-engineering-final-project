package it.polimi.ingsw.gui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Exceptions.PlayerNotFoundException;
import it.polimi.ingsw.client.Game.PlayerBoard;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.gui.supportClass.*;
import it.polimi.ingsw.server.Model.MainBoard;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Cards.CardColor;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;
import it.polimi.ingsw.shared.JsonSupportClasses.Position;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.shared.PlayerMode;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static it.polimi.ingsw.shared.Cards.CardColor.BLUE;
import static it.polimi.ingsw.shared.Cards.CardColor.EMPTY;
import static javafx.geometry.Pos.CENTER;


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


    /**
     * @param url
     * @param resourceBundle used to take the resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imagesInit(podiumImage, "podium.png"); //load player board img
        imagesInit(logoImage, "Title.png");
    }

    /**
     * @param points is the json object for the player points
     */
    public void endgamePoints(JsonObject points){

        PlayingPlayer player = (PlayingPlayer) helloApplication.getPlayer();
        Scanner sc = new Scanner(System.in);
        char selection;
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
    @FXML
    protected void backToLobby(ActionEvent actionEvent){
        helloApplication.getConnection().setPlayerAsLobby();
        helloApplication.setMode(PlayerMode.LOBBY);
    }

}
