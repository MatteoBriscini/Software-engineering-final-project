package it.polimi.ingsw.JsonSupportClasses;

import it.polimi.ingsw.Cards.CardColor;

public class PositionWithColor {
    private final int x;
    private final int y;

    private final CardColor color;


    public PositionWithColor(int x, int y, CardColor color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CardColor getColor() {
        return color;
    }
}
