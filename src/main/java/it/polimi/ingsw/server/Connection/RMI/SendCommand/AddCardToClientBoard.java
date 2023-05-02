package it.polimi.ingsw.server.Connection.RMI.SendCommand;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.polimi.ingsw.client.Connection.PlayingPlayerRemoteInterface;
import it.polimi.ingsw.shared.Cards.Card;

import java.rmi.RemoteException;

public class AddCardToClientBoard  implements Command{
    private final String playerID;
    private final int column;
    private String cards;
    public AddCardToClientBoard(String playerID, int column, Card[] cards){
        Gson gson = new Gson();
        JsonArray jsonArray = new Gson().toJsonTree(cards).getAsJsonArray();

        this.playerID = playerID;
        this.column = column;
        this.cards = jsonArray.toString();
    }

    public boolean execute(PlayingPlayerRemoteInterface client) {
        try {
            client.addCardToPlayerBoard(playerID, column, cards);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
