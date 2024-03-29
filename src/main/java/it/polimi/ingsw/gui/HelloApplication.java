package it.polimi.ingsw.gui;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.client.View.UserInterface;
import it.polimi.ingsw.gui.supportClass.Message;
import it.polimi.ingsw.gui.supportClass.MessageTipe;
import it.polimi.ingsw.gui.supportClass.Title;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.shared.PlayerMode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class HelloApplication extends Application implements UserInterface {

    private GuiView guiView;
    private WaitingroomController  waitingroomController;
    private Player player;
    private ConnectionManager connection;
    private Stage stage;
    private Parent root;


    public Stage getStage() {
        return stage;
    }

    public Parent getRoot() {
        return root;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setConnection(ConnectionManager connection) {
        this.connection = connection;
    }

    public ConnectionManager getConnection() {
        return connection;
    }
    private final ArrayList<Title> titles = new ArrayList<>();
    public ArrayList<Title> getTitles() {
        return titles;
    }


    /**
     * change java fx scene
     * @param viewName name of the scene have to set up
     * @throws IOException if the scene isn't available
     */
    public void changeView(String viewName) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(viewName));
        root = (Parent) fxmlLoader.load() ;
        guiView = (GuiView) fxmlLoader.getController();
        guiView.setHelloApplication(this);
        Scene scene = new Scene(root, 1280, 720);
        String css = this.getClass().getResource("application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Shelfie!");
        stage.setScene(scene);
        stage.show();

    }


    /**
     * @param stage the primary stage for this application, onto which the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be primary stages.
     */
    @Override
    public void start(Stage stage) throws IOException {

        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("hello-view.fxml"));

        //FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("endgame.fxml"));
        root = (Parent) fxmlLoader.load() ;
        guiView = (GuiView) fxmlLoader.getController();
        guiView.setHelloApplication(this);
        Scene scene = new Scene(root, 1280, 720);
        stage.getIcons().add(new Image(this.getClass().getClassLoader().getResourceAsStream("Icon.png")));
        String css = this.getClass().getResource("application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("My Shelfie!");
        stage.setScene(scene);
        stage.show();
        this.stage = stage;

    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * @param tableJ json object used for get the points at the end
     */
    @Override
    public void finalResults(JsonObject tableJ) {
        Platform.runLater(()-> {
            try {
                changeView("endgame.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ((EndgameController)guiView).endgamePoints(tableJ);
        });
    }

    /**
     * called from the server during waiting room phase
     * @param n num player in the game
     */
    @Override
    public void receiveNumPlayers(int n) {
        Platform.runLater(() -> ((WaitingroomController) guiView).changeNumPlayer(n));
    }

    /**
     * called from the server when there is new chat messages
     * @param msg chat message
     */
    @Override
    public void receiveMsg(String msg) {
        Message message;
        if(msg.contains("[PRIVATE]"))message =new Message(msg, MessageTipe.PRIVATE);
        else message=new Message(msg, MessageTipe.BROADCAST);
        Platform.runLater(() -> {
            if(guiView instanceof GameController) ((GameController)guiView).chatReceiveMsg(message);
        });
    }

    /**
     * called from the server when it is necessary to recreate all the game view
     */
    @Override
    public void updateAll() {
        if(guiView instanceof GameController) {
            Platform.runLater(() -> {
                ((GameController)guiView).setMainBoard();
            });
            return;
        }

        Platform.runLater(() -> {
            player = connection.getPlayer();

            stage.setResizable(false);
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("game.fxml"));
            try {
                root = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            guiView = (GuiView) fxmlLoader.getController();
            guiView.setHelloApplication(this);
            ((GameController)guiView).gameInit();
            Scene scene = new Scene(root, 1550, 750);
            stage.setScene(scene);
            String css = this.getClass().getResource("game.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.show();
        });
    }

    /**
     * @param p used to identify the cards taken from th livingroom
     */
    @Override
    public void updateMainBoard(PositionWithColor[] p) {
        Platform.runLater(() -> {
            if(guiView instanceof GameController)((GameController)guiView).updateMainBoard(p);
        });
    }

    /**
     * @param id used to identify the player needed
     * @param column used to understand which column was selected to insert the tails
     * @param c used to identify the cards to put in the bookshelf
     */
    @Override
    public void updatePlayerBoard(String id, int column, Card[] c) {
        Platform.runLater(() -> {
            if(guiView instanceof GameController)((GameController) guiView).updatePlayerBoard(id,column,c);
        });
    }

    /**
     * called from server when a common goal is scored
     */
    @Override
    public void updateLastCommonGoal() {
        Platform.runLater(()->{
            ((GameController)guiView).updateLastCommonGoal();
        });
    }

    /**
     * @param m passed to get the player mode
     */
    @Override
    public void setMode(PlayerMode m) {
        player = connection.getPlayer();
        if(guiView instanceof GameController) ((GameController)guiView).musicStop();
        if(m.equals(PlayerMode.LOBBY))Platform.runLater(() -> {
            try {
                this.changeView("creategame.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * called from server when an error occurs, this method print the message error
     * @param s error message
     */
    @Override
    public void printError(String s) {
        Platform.runLater(() ->guiView.errorMsg(s));
    }

    /**
     * start to accept game command
     */
    @Override
    public void acceptingPlayingCommand() {}

    /**
     * called from server when the current player change
     */
    @Override
    public void notifyNewActivePlayer() {
        Platform.runLater(() -> {
            if(guiView instanceof GameController)((GameController)guiView).notifyNewActivePlayer();
        });
    }

    /**
     * kill the application
     */
    @Override
    public void stop(){
        System.exit(0);
    }

}
