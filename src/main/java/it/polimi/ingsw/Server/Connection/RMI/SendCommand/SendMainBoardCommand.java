package it.polimi.ingsw.Server.Connection.RMI.SendCommand;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.ingsw.Client.Connection.PlayingPlayerRemoteInterface;

import it.polimi.ingsw.Shared.Cards.Card;

import java.rmi.RemoteException;

public class SendMainBoardCommand implements Command {
    String mainBoard;
    public SendMainBoardCommand(Card[][] mainBoard){
        Gson gson = new Gson();
        JsonArray jsonArray = new Gson().toJsonTree(mainBoard).getAsJsonArray();
        this.mainBoard = jsonArray.toString();
    }

    @Override
    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.receiveMainBoard(mainBoard);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
