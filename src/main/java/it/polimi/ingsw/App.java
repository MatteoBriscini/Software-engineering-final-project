package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.gui.Launcher;
import it.polimi.ingsw.server.ServerMain;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        if(args.length == 0){
            Launcher.main(args);
            return;
        }
        switch (args[0]){
            case "--server":
                ServerMain.main(args);
                break;
            case "--tui":
                ClientMain.main(args);
                break;
            default:
                Launcher.main(args);
                break;
        }
    }
}
