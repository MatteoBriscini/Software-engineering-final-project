package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;

public class DellCardFromMainBoard implements Command{
    String cards;
    public DellCardFromMainBoard(PositionWithColor[] cards){
        Gson gson = new Gson();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();

        this.cards = jsonArray .toString();
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.removeCardFromMainBoard(cards);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
