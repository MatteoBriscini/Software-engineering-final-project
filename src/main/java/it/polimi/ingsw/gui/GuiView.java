package it.polimi.ingsw.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public abstract class GuiView {
    HelloApplication helloApplication;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Label label = new Label();

    MediaPlayer buttonAudio;

    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }

    /**
     * print error message in a popup
     * @param errorMsg error message
     */
    public void errorMsg(String errorMsg){
        alert.setTitle("my shelfie");
        alert.setContentText(errorMsg);
        alert.show();
    }

    /**
     * audio effect for button click
     */
    protected void buttonClickedAudio(){
        if(buttonAudio == null) {
            Media click = new Media(this.getClass().getClassLoader().getResource("page-flip-10.wav").toExternalForm());
            buttonAudio = new MediaPlayer(click);
        }
        buttonAudio.play();
    }

    /**
     * @param imageView UI element where has to show image
     * @param file used to get the correct file from the resources
     */
    protected void imagesInit(ImageView imageView, String file){
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(file));
        imageView.setImage(image);
    }

}
