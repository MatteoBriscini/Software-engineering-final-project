package it.polimi.ingsw.Server.JsonSupportClasses;

import it.polimi.ingsw.Server.Model.Cards.CardColor;

public class PositionWithColor extends Position{
    private final int sketch;
    private final CardColor color;


    public PositionWithColor(int x, int y, int sketch, CardColor color){
        super(x,y);
        this.sketch = sketch;
        this.color = color;
    }

    public int getSketch() {
        return sketch;
    }

    public CardColor getColor() {
        return color;
    }
}
