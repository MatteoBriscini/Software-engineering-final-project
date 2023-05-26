module AM19 {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires com.google.gson;
    requires javafx.media;

    //Method/exception InvocationTargetException;

    opens it.polimi.ingsw.server to com.google.gson, java.rmi;
    opens it.polimi.ingsw.server.Model.PlayerClasses to com.google.gson, java.rmi;
    opens it.polimi.ingsw.client to com.google.gson, java.rmi;
    opens it.polimi.ingsw.shared.Cards to com.google.gson, java.rmi;
    opens it.polimi.ingsw.shared.Connection to com.google.gson, java.rmi;
    opens it.polimi.ingsw.shared.JsonSupportClasses to com.google.gson, java.rmi;
    opens it.polimi.ingsw.gui to com.google.gson, java.rmi, javafx.fxml;

    exports it.polimi.ingsw.server.Connection.RMI;
    exports it.polimi.ingsw.shared.exceptions;
    exports it.polimi.ingsw.server.Connection;
    exports it.polimi.ingsw.client.Connection;
    exports it.polimi.ingsw.gui;

}