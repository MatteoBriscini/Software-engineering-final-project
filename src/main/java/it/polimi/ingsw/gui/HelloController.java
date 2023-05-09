package it.polimi.ingsw.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
@FXML
private Label passwordLabel;

@FXML
private Label usernameLabel;

@FXML
private Label errorLabel;








    @FXML
    protected void onHelloButtonClick() {
        this.errorLabel.setText("");
        if(!this.usernameLabel.getText().matches("example")) this.errorLabel.setText("password or username incorrect");
        else this.errorLabel.setText("welcome");
        if(!this.passwordLabel.getText().matches("password")) this.errorLabel.setText("password or username incorrect");
        else{
            this.errorLabel.setText("welcome");
            //to do... new scene
        }

    }



}
