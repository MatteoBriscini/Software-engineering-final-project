package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;

import java.rmi.RemoteException;
import java.util.Map;

public class SendPrivateGoalCommand extends CommandAbstract implements Command{

    String cards;
    String playerID;
    public SendPrivateGoalCommand(PositionWithColor[] cards, String playerID, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);
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
