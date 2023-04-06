package it.polimi.ingsw.Server.Connection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;

public abstract class ConnectionController {
    protected final Controller controller;
    protected final int PORT;

    public ConnectionController (Controller controller, int port){
        this.controller = controller;
        this.PORT = port;
    }

    public abstract void connection();

    public abstract void notifyActivePlayer(String activePlayerID);

    public boolean startGame(String playerID) throws RemoteException {
        return controller.startGame(playerID);
    }

    public boolean takeCard (int column, JsonArray cards ,String playerID) throws RemoteException {
        PositionWithColor[] cardsMatrix = new Gson().fromJson(cards, PositionWithColor[].class);
        return controller.takeCard(column, cardsMatrix, playerID);
    }

}
