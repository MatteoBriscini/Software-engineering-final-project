package it.polimi.ingsw.client.View;

import com.google.gson.JsonObject;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.JsonSupportClasses.PositionWithColor;
import it.polimi.ingsw.shared.PlayerMode;

public interface UserInterface {

    void finalResults(JsonObject tableJ);

    void receiveNumPlayers(int n);

    void receiveMsg(String msg);

    void updateAll();

    void updateMainBoard(PositionWithColor[] p);

    void updatePlayerBoard(String id, int column, Card[] c);

    void setMode(PlayerMode m);

    void printError(String s);

    void acceptingPlayingCommand();

}
