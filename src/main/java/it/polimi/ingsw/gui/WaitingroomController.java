package it.polimi.ingsw.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitingroomController extends GuiView implements Initializable {

    @FXML
    private Label playerGamesLabel = new Label();

    @FXML
    private ImageView titleImage = new ImageView();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream("Title.png"));
        titleImage.setImage(image);
    }

    /**
     * start the game
     */
    @FXML
    protected void startClick(ActionEvent actionEvent){
        this.buttonClickedAudio();
        Player player = helloApplication.getPlayer();

        ((PlayingPlayer)player).startGame();

    }

    /**
     * called from the server when a new player log the game, print the number of the player in the game
     */
    public void changeNumPlayer(int num){
        playerGamesLabel.setText("PLAYERS IN GAME: " + num);
    }


}
