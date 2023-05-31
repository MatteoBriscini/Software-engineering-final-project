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

    /**
     * @param resourceBundle is the bundle in which the resources are taken
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream("Title.png"));
        titleImage.setImage(image);
    }

    @FXML
    protected void startClick(ActionEvent actionEvent){
        this.buttonClickedAudio();
        Player player = helloApplication.getPlayer();

        ((PlayingPlayer)player).startGame();

    }

    public void changeNumPlayer(int num){
        playerGamesLabel.setText("PLAYER GAMES: " + num);
    }


}
