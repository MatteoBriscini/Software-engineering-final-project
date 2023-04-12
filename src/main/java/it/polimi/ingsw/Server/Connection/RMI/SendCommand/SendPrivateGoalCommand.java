package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.Shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;

public class SendPrivateGoalCommand implements Command{

    String cards;
    String playerID;
    public SendPrivateGoalCommand(PositionWithColor[] cards, String playerID){
        Gson gson = new Gson();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();

        this.cards = jsonArray .toString();
        this.playerID = playerID;
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.receivePrivateGoal(cards,playerID);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
