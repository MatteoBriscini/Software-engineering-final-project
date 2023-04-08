package it.polimi.ingsw.Server.Lobby;

import junit.framework.TestCase;

import javax.security.auth.login.LoginException;

public class LobbyTest extends TestCase {


    Lobby testLobby = new Lobby();


    public void testLogin() {

        String ID = "john";
        String pwd = "doe";

        try {
            testLobby.login(ID, pwd);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }

    }

    public void testSignUp() {

        String ID = "rat";
        String pwd = "king";

        testLobby.signUp(ID, pwd);

    }

    public void testJoinGame() {
    }
}