package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.shared.Cards.Card;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class SendAllPlayerBoardCommand extends CommandAbstract implements Command{

    String playerBoards;

    public SendAllPlayerBoardCommand(ArrayList<Card[][]> playerBoards, Map<String, PlayingPlayerRemoteInterface> clients, Controller connectionInterface){
        super(clients, connectionInterface);
        JsonArray jsonArray = new Gson().toJsonTree(playerBoards).getAsJsonArray();
        this.playerBoards = jsonArray.toString();
    }
    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.receiveAllPlayerBoard(playerBoards);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
