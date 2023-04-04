package it.polimi.ingsw.Server.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LobbySOCKET extends LobbyRMI{


    public LobbySOCKET(int port) {
        super(port);
    }

    synchronized public void connection() {

        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(PORT);
        }catch (IOException e){
            System.out.println(e.toString());
            return;
        }

        System.err.println("Server (socket) for Lobby ready on port: " + PORT);

        while(true){
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new MultiClientSocketLobby(socket));
            }catch(IOException e){
                System.out.println(e.toString());
                break;
            }
        }

        executor.shutdown();

    }


    private class MultiClientSocketLobby implements Runnable{

        private final Socket socket;


        public MultiClientSocketLobby(Socket socket){this.socket = socket;}

        public void run(){

        }

    }
}
