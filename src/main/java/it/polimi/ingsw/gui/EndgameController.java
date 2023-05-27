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
    private VBox placing = new VBox();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imagesInit(podiumImage, "podium.png"); //load player board img
    }

    public void endgamePoints(JsonObject points){

        System.out.println("0");
        TreeMap<String, Integer> pointsMap = new TreeMap<>();
        PlayingPlayer player = (PlayingPlayer) helloApplication.getPlayer();
        String[] players = player.getPlayersID();

        System.out.println("1");
        for(String s: players){
            pointsMap.put(s, points.get(s).getAsInt());

        }

        System.out.println("2");
        System.out.println(pointsMap);
       // pointsMap = ((i,j)->)
        for(String s: pointsMap.keySet()){
            Label label = new Label();
            label.setText(s + ": " + pointsMap.get(s));
            placing.getChildren().add(label);
        }
    }

}
