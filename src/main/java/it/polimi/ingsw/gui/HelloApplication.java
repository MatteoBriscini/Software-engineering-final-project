package it.polimi.ingsw.gui;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.client.View.UserInterface;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.shared.PlayerMode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application implements UserInterface {

    GuiView guiView;


    public void changeView(String viewName) throws IOException {

        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(viewName));
        Parent root = (Parent) fxmlLoader.load() ;
        guiView = (GuiView) fxmlLoader.getController();
        guiView.setHelloApplication(this);
        Scene scene = new Scene(root, 580, 500);
        stage.setTitle("Shelfie!");
        stage.setScene(scene);
        stage.show();


       /* Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(viewName));
        Parent root = (Parent) fxmlLoader.load();
        guiView = (HelloController)fxmlLoader.getController();
        guiView.setHelloApplication(this);
        Scene scene = new Scene(root, 580, 500);
        stage.setScene(scene);
        stage.show(); */

    }



    @Override
    public void start(Stage stage) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("hello-view.fxml"));
            Parent root = (Parent) fxmlLoader.load() ;
            guiView = (HelloController) fxmlLoader.getController();
            guiView.setHelloApplication(this);
            Scene scene = new Scene(root, 320, 240);
            stage.setTitle("Shelfie!");
            stage.setScene(scene);
            stage.show();



    }

    public static void main(String[] args) {

        if (args.length > 0) {
            String param0 = args[0];
            if (param0.equals( "--server") )
                runAsServer();
        }else {

            launch();
        }
    }

    static void runAsServer(){
        System.out.println("started as server.. bye!");
    }

    @Override
    public void finalResults(JsonObject tableJ) {

    }

    @Override
    public void receiveNumPlayers(int n) {

    }

    @Override
    public void receiveMsg(String msg) {

    }

    @Override
    public void updateAll() {

    }

    @Override
    public void updateMainBoard(PositionWithColor[] p) {

    }

    @Override
    public void updatePlayerBoard(String id, int column, Card[] c) {

    }

    @Override
    public void setMode(PlayerMode m) {

    }

    @Override
    public void printError(String s) {

    }
}
