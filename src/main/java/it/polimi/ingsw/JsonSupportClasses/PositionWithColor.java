package it.polimi.ingsw.JsonSupportClasses;

public class PositionWithColor {
    private final int x;
    private final int y;

    private final String color;


    public PositionWithColor(int x, int y, String color){
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

    public String getColor() {
        return color;
    }
}
