package it.polimi.ingsw.client.View;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Cards.CardColor;
import it.polimi.ingsw.shared.Connection.ConnectionType;
import junit.framework.TestCase;

import java.rmi.RemoteException;

public class TUITest extends TestCase {

    ClientMain cm=new ClientMain();
    PlayingPlayer player;
    Card[][] mainBoard=new Card[6][5];

    Card[] fillBoard={  new Card(CardColor.BLUE),new Card(CardColor.YELLOW),new Card(CardColor.GREEN),new Card(CardColor.LIGHTBLUE),new Card(CardColor.BLUE),
                        new Card(CardColor.PINK),new Card(CardColor.BLUE),new Card(CardColor.WHITE),new Card(CardColor.BLUE),new Card(CardColor.PINK),
                        new Card(CardColor.BLUE),new Card(CardColor.GREEN),new Card(CardColor.BLUE),new Card(CardColor.WHITE),new Card(CardColor.BLUE),
                        new Card(CardColor.EMPTY),new Card(CardColor.BLUE),new Card(CardColor.YELLOW),new Card(CardColor.WHITE),new Card(CardColor.GREEN),
                        new Card(CardColor.BLUE),new Card(CardColor.YELLOW),new Card(CardColor.BLUE),new Card(CardColor.BLUE),new Card(CardColor.BLUE),
                        new Card(CardColor.BLUE),new Card(CardColor.BLUE),new Card(CardColor.EMPTY),new Card(CardColor.BLUE),new Card(CardColor.WHITE)
                    };

    {
        try {
            player = new PlayingPlayer("name","pwd",cm, ConnectionType.DEBUG,0,"Server");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    TUI tui;

    public void testMethod() {

        for(int i=0;i< fillBoard.length;i++){
            mainBoard[i/5][i%5]=fillBoard[fillBoard.length-i-1];
        }

        player.createMainBoard(mainBoard);
        tui=new TUI(player);
        tui.printBoard();
    }
}