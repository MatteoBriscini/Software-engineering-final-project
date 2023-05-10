package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;

import it.polimi.ingsw.shared.Cards.Card;

import java.rmi.RemoteException;

public class SendMainBoardCommand implements Command {
    String mainBoard;
    public SendMainBoardCommand(Card[][] mainBoard){
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