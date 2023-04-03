package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.Server.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerSOCKET extends ControllerRMI{
    public ControllerSOCKET(Controller controller, int port){
        super(controller, port);
    }

    @Override
    synchronized public void connection(){
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(PORT);  //throw exception if unavailable port
        } catch (IOException e) {
            System.out.println(e.toString());           //da finire*********************************
            return;
        }
        System.err.println("Server (socket) for newGame ready on port: " + PORT);

        while (true){
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new MultiClientSocketGame(socket));
            } catch (IOException e) {

                System.out.println("test");
                System.out.println(e.toString()); //go here if socket is closed
                break;//da finire*********************************
            }
        }
        executor.shutdown();
    }

     private class MultiClientSocketGame implements Runnable{
        private final Socket socket;

        public MultiClientSocketGame(Socket socket){
            this.socket = socket;
        }

        public void run(){
            //go here when players connect to the server
            try {
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                while (true){
                    String line = in.nextLine();
                    if (line.equals("quit")) {
                        break;
                    } else {
                        out.println("Received: " + line); //response
                        System.out.println(line);
                        out.flush();
                    }
                }
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }
}
