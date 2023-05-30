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





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imagesInit(podiumImage, "podium.png"); //load player board img
        imagesInit(logoImage, "Title.png");
    }

    public void endgamePoints(JsonObject points){

        TreeMap<String, Integer> pointsMap = new TreeMap<>();
        PlayingPlayer player = (PlayingPlayer) helloApplication.getPlayer();
        String[] players = player.getPlayersID();

        for(String s: players){
            pointsMap.put(s, points.get(s).getAsInt());
        }
        for(int i = pointsMap.keySet().size()-1; i>0;i--){
            Label label = new Label();
            String s = (String) pointsMap.keySet().toArray()[i];
            label.setText(s + ": " + pointsMap.get(s));
            if (s.equals(player.getPlayerID())) label.setId("yourScore");
            placing.getChildren().add(label);
        }
        p1.setText((String) pointsMap.keySet().toArray()[1]);
        p2.setText((String) pointsMap.keySet().toArray()[0]);

        if(players.length > 2) {
            p1.setText((String) pointsMap.keySet().toArray()[2]);
            p2.setText((String) pointsMap.keySet().toArray()[1]);
            p3.setText((String) pointsMap.keySet().toArray()[0]);
        }else
            podium.getChildren().remove(p3);

    }
    @FXML
    protected void backToLobby(ActionEvent actionEvent){
        helloApplication.getConnection().setPlayerAsLobby();
        helloApplication.setMode(PlayerMode.LOBBY);
    }

}
