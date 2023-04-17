package it.polimi.ingsw.Client.Game;

import it.polimi.ingsw.Shared.Cards.Card;
import it.polimi.ingsw.Shared.Cards.CardColor;

public class MainBoard {
    Card[][] board;
    public MainBoard(Card[][] board){
        this.board = board;
    }
    public CardColor getColor(int x, int y){
        return board[x][y].getColor();
    }

    public int getSketch(int x, int y){
        return board[x][y].getSketch();
    }
}
