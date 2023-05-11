package it.polimi.ingsw.client.View;

import com.google.gson.JsonObject;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.shared.PlayerMode;

public interface UserInterface {

    public abstract void finalResults(JsonObject tableJ);

    public abstract void receiveNumPlayers(int n);

    public abstract void receiveMsg(String msg);

    public abstract void updateAll();

    public abstract void updateMainBoard(PositionWithColor[] p);

    public abstract void updatePlayerBoard(String id, int column, Card[] c);

    public abstract void setMode(PlayerMode m);

    public abstract void printError(String s);

}
