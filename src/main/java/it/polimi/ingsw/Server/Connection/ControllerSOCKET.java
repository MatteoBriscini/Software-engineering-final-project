package it.polimi.ingsw.Server.Connection;

import it.polimi.ingsw.Server.Controller;

import java.io.IOException;
import java.io.PrintWriter;

import java.lang.reflect.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerSOCKET extends ControllerRMI{
    public ControllerSOCKET(Controller controller, int port){
        super(controller, port);
    }

    public boolean getName(String C) {
        System.out.println(C + " on port: "+ PORT);
        return false;
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
                boolean bool;
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                while (true){
                    String line = in.nextLine();
                    if (line.equals("quit")) {
                        break;
                    } else {
                        bool = true;

                        //da finire method name non dovra essere line ma un solo parametro di line
                        String methodName = line;
                        Method getNameMethod = null;
                        try {
                            getNameMethod = ControllerSOCKET.this.getClass().getMethod(methodName, String.class);
                        } catch (NoSuchMethodException e) {
                            bool = false;
                        }

                        System.out.println(bool);

                        if(bool == true) {
                            try {
                                boolean name = (boolean) getNameMethod.invoke(ControllerSOCKET.this, "Mishka");
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                bool = false;
                                System.out.println(bool);
                            }
                        }

                        out.println(bool); //response
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
