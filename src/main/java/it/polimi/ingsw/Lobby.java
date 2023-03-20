package it.polimi.ingsw;

public class Lobby {
    private List<Player> players;
    private Player creator;
    private boolean gameStarted;

    // Constructor
    public Lobby() {
        this.players = new ArrayList<Player>();
        this.creator = null;
        this.gameStarted = false;
    }



    // Method to add a player to the lobby
    public void addPlayer(Player player) {
        if (!gameStarted && !players.contains(player)) { // can only add players if game has not started and player is not already in the lobby
            players.add(player);
            if (creator == null) { // assign power to create, end and invite to the first player who joins
                creator = player;
                creator.setPowerToCreateGame(true);
            }
        }
    }

    // Method to remove a player from the lobby
    public void removePlayer(int playerId) {
        if (!gameStarted) { // can only remove players if game has not started
            for (Player player : players) {
                if (player.getId() == playerId) {
                    players.remove(player);
                    if (player.equals(creator)) { // if the creator leaves, assign the power to create to the next player who joined
                        if (!players.isEmpty()) {
                            creator = players.get(0);
                            creator.setPowerToCreateGame(true);
                        } else {
                            creator = null;
                        }
                    }
                    break;
                }
            }
        }
    }

    // Method to start the game
    public void startGame() {
        if (!gameStarted && creator != null) { // can only start game if no game is already started and a creator has been assigned
            gameStarted = true;
            // Do something to start the game...
        }
    }

    // Method to end the game
    public void endGame() {
        if (gameStarted) { // can only end game if game has started
            gameStarted = false;
            // Do something to end the game...
        }
    }


    public static void main(String[] args) {
        // Create a lobby
        Lobby lobby = new Lobby();

        // still need to understand how made other players join with and without invitation, so I create them atm
        Player alice = new Player(1, "Alice", false);
        Player bob = new Player(2, "Bob", false);
        Player charlie = new Player(3, "Charlie", false);
        Player dave = new Player(4, "Dave", true); // Dave is the first to join, so he has the power to create games

        // Add players to the lobby
        lobby.addPlayer(dave);
        lobby.addPlayer(alice);
        lobby.addPlayer(bob);
        lobby.addPlayer(charlie);

        // Start the game (should not work because no player has power to create game)
        lobby.startGame();

        // Assign the power to create game to Bob
        bob.setPowerToCreateGame(true);

        // Start the game (should work now)
        lobby.startGame();

        // End the game
        lobby.endGame();

        // Remove Bob from the lobby and assign power to create the game to Alice
        lobby.removePlayer(bob);
        alice.setPowerToCreateGame(true);
    }
}

