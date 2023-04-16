package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.Shared.Cards.Card;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class SendAllPlayerBoardCommand implements Command{

    String playerBoards;

    public SendAllPlayerBoardCommand(ArrayList<Card[][]> playerBoards){
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
