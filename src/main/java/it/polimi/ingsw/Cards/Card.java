package it.polimi.ingsw.Cards;

public class Card {
    private CardColor color;
    private int sketch;


    public Card(CardColor color,int sketch){
        this.color=color;
        this.sketch=sketch;
    }

    public CardColor getColor(){
        return this.color;
    }

    public int getSketch(){
        return this.sketch;
    }

    public int hashCode(){  //make possible to insert card in hashset
        switch (color){
            case BLUE:return 1;
            case GREEN: return 2;
            case LIGHTBLUE: return 3;
            case PINK: return 4;
            case WHITE: return 5;
            case YELLOW: return 6;
            default: return 0; //default == empty
        }
    }

    public boolean equals (Card other){ //make possible to insert card in hashset
        return this.getColor() == other.getColor();
    }
}
