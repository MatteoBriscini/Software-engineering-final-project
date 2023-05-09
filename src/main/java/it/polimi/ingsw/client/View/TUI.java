package it.polimi.ingsw.client.View;

import it.polimi.ingsw.client.Game.MainBoard;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Cards.CardColor;
import java.util.*;

import static it.polimi.ingsw.client.View.ColorCodes.*;


public class TUI {

    private Player player;
    private MainBoard mainBoard;

    private String printLine;

    public TUI(Player player){
        this.player=player;
    }

    public void printBoard() {



        mainBoard=((PlayingPlayer)player).getMainBoard();


        for(int x=0;x< mainBoard.getColumns();x++) {
            printLine= EMPTY.get();
            for (int y = mainBoard.getRows() - 1; y >= 0; y--) {
                for (int i = 0; i < CardColor.values().length; i++) {
                    if(mainBoard.getColor(x, y).toString().equals(CardColor.values()[i].toString())) {
                        printLine = printLine + ColorCodes.values()[i].get()+"  ";
                    }
                }
            }
            System.out.println(printLine + " " + EMPTY.get());
        }
        System.out.println("Main board");
        System.out.println((player).getPlayerID().toString()+"'s board");
    }
}
