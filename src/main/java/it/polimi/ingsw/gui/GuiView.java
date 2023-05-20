package it.polimi.ingsw.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public abstract class GuiView {
    HelloApplication helloApplication;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Label label = new Label();

    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }

    public void errorMsg(String errorMsg){
        alert.setTitle("my shelfie");
        alert.setContentText(errorMsg);
        alert.show();
    }
}
