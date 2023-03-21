package it.polimi.ingsw;

import java.util.ArrayList;

public class Lobby {
    private int[][] activeGame; // matrix with ID of active game in first column and players in remaining columns
    private ArrayList<Player> allPlayers; // list of all players

    public Lobby() {
        activeGame = new int[10][5]; // assume 10 active games with up to 4 players each (random choice)
        allPlayers = new ArrayList<Player>;
    }

    // login method to check the username and password and add player to active game if possible
    public boolean login(String username, String password) {
        for (Player p : allPlayers) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                // check if player is already in an active game
                for (int i = 0; i < activeGame.length; i++) {
                    for (int j = 0; j < activeGame[i].length; j++) {
                        if (activeGame[i][j] != 0 && activeGame[i][j] == p.getID()) {
                            // player is in an active game, add them back to it
                            return true;
                        }
                    }
                }
                // player is not in an active game, allow login
                return true;
            }
        }
        // 404 player not found or password incorrect
        return false;
    }

    // registration method to add new player to allPlayers list
    public boolean registration(String username, String password) {
        // check if username already exists
        for (Player p : allPlayers) {
            if (p.getUsername().equals(username)) {
                return false;
            }
        }
        // add new player to list
        allPlayers.add(new Player(username, password, allPlayers.size() + 1));
        return true;
    }

    // create game method to add new game to activeGame matrix
    public boolean createGame(int ownerID) {
        // find first empty row in matrix
        for (int i = 0; i < activeGame.length; i++) {
            if (activeGame[i][0] == 0) {
                activeGame[i][0] = ownerID; // set owner ID in first column (is the payer with god power)
                return true;
            }
        }
        // no empty rows found
        return false;
    }

    // end game method to remove game from activeGame matrix
    public boolean endGame(int gameID, int ownerID) {
        // find game with matching ID and owner
        for (int i = 0; i < activeGame.length; i++) {
            if (activeGame[i][0] == gameID && activeGame[i][1] == ownerID) {
                activeGame[i] = new int[6]; // clear row
                return true;
            }
        }
        // game not found or owner ID incorrect
        return false;
    }

    // join game method to add player to existing game
    public boolean joinGame(int gameID, int playerID) {
        // find game with matching ID and room for player
        for (int i = 0; i < activeGame.length; i++) {
            if (activeGame[i][0] == gameID) {
                for (int j = 1; j < activeGame[i].length; j++) {
                    if (activeGame[i][j] == 0) {
                        activeGame[i][j] = playerID;
                        return true;
                    }
                }
            }
        }
        // game not found or no room for player
        return false;
    }
}

