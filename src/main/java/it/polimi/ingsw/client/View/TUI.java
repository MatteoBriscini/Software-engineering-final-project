package it.polimi.ingsw.client.View;

import it.polimi.ingsw.client.Connection.ClientRMI;
import it.polimi.ingsw.client.Connection.ClientSOCKET;
import it.polimi.ingsw.client.Connection.ConnectionManager;
import it.polimi.ingsw.client.Game.MainBoard;
import it.polimi.ingsw.client.Game.PlayerBoard;
import it.polimi.ingsw.client.Player.LobbyPlayer;
import it.polimi.ingsw.client.Player.Player;
import it.polimi.ingsw.client.Player.PlayingPlayer;
import it.polimi.ingsw.shared.Cards.Card;
import it.polimi.ingsw.shared.Cards.CardColor;
import it.polimi.ingsw.shared.Connection.ConnectionType;

import java.net.Socket;
import java.util.*;

import static it.polimi.ingsw.client.View.ColorCodes.*;
import static it.polimi.ingsw.shared.Connection.ConnectionType.*;


public class TUI {

    private Player player;

    private String printLine;

    private ConnectionManager connection;

    public TUI(Player player){
        this.player=player;
    }



    public void toRun(){
        System.out.println("WELCOME TO MYSHELFIE");
        connectionSelection();
        userIdentification();
    }

    public void connectionSelection(){

        Scanner sc = new Scanner(System.in);
        char c;

        do {
            System.out.println("Select the type of the connection:\n[R] RMI\n[S] Socket");
            c = sc.next().charAt(0);
            if(c!='R' && c!='S')
                System.out.println("Invalid selection, please try again\n");
        }while(c!='R' && c!='S');

        if(c=='S'){
            try {
                connection = new ClientSOCKET(1234,"127.0.0.1");
            } catch (Exception e) {
                System.out.println("Server is offline");
            }
        }
        else{
            try {
                connection = new ClientRMI(1234,"127.0.0.1");
            } catch (Exception e) {
                System.out.println("Server is offline");
            }
        }
    }

    public void userIdentification() {

        Scanner sc = new Scanner(System.in);
        String user;
        String pwd;
        char selection;
        boolean logged;

        user="/back";
        selection='n';
        do {
            if(user.equals("/back")) {
                do {
                    System.out.println("What do you want to do?\n[S]Sign up\n[L]Log in");
                    selection = sc.next().charAt(0);
                    if (selection != 'T' && selection != 'G')
                        System.out.println("Invalid selection, please try again\n");
                } while (selection != 'S' && selection != 'L');
            }
            logged=false;
            System.out.println("Enter your username (or /back to return to the previous selection):");
            user=sc.nextLine();
            if(!user.equals("/back")) {
                System.out.println("Enter your password:");
                pwd = sc.nextLine();

                player = new LobbyPlayer(user, pwd, connection);
                if (selection == 'S') {
                    logged = ((LobbyPlayer) player).signUp();
                } else {
                    logged = ((LobbyPlayer) player).login();
                }
            }
        }while (!logged || user.equals("/back"));

    }

    public void printBoard() {

        MainBoard mainBoard;
        PlayerBoard playerBoard;

        for(String id : ((PlayingPlayer) player).getPlayersID()){
            playerBoard = ((PlayingPlayer)player).getPlayerBoard(id);
            for(int x=0;x< playerBoard.getColumns();x++) {
                printLine= EMPTY.get();
                for (int y = playerBoard.getRows() - 1; y >= 0; y--) {
                    for (int i = 0; i < CardColor.values().length; i++) {
                        if(playerBoard.getColor(x, y).toString().equals(CardColor.values()[i].toString())) {
                            printLine = printLine + ColorCodes.values()[i].get()+"  ";
                        }
                    }
                }
                System.out.println(printLine + " " + EMPTY.get());
            }
            if(id.equals(player.getPlayerID()))
                System.out.println("Your board");
            else
                System.out.println(id+"'s board");


            //System.out.println((player).getPlayerID().toString()+"'s board");


        }

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

    }


    public void createGame(){
        Scanner sc = new Scanner(System.in);
        String selection;

        do {
            System.out.println("Insert the number of players for this game (min 2, max 4)\nInsert /def to use default settings");
            selection = sc.nextLine();
            if (selection.equals("/def")) {
                ((LobbyPlayer)player).createGame();
            }
            if(Integer.parseInt(selection)>=2 && Integer.parseInt(selection)<=4)
                ((LobbyPlayer)player).createGame(Integer.parseInt(selection));
            else System.out.println("Invalid selection, please try again");
        }while(Integer.parseInt(selection)<2 || Integer.parseInt(selection)>4);




    }


    public void sendMsg(){
        Scanner sc = new Scanner(System.in);
        //((PlayingPlayer)player).sendMsg(sc.nextLine());
    }

    public void receiveMsg(String msg){
        System.out.println(msg);
    }
}
