module AM19 {

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires com.google.gson;

    //Method/exception InvocationTargetException;

    opens it.polimi.ingsw.client.userinterface to com.google.gson, java.rmi;
    opens it.polimi.ingsw.Server  to com.google.gson, java.rmi;
    opens it.polimi.ingsw.client to com.google.gson, java.rmi;
    opens it.polimi.ingsw.gui to com.google.gson, java.rmi, javafx.fxml;

    exports it.polimi.ingsw.gui;


}