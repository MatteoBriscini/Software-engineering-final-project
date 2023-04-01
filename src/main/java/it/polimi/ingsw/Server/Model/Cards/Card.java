package it.polimi.ingsw.Server.Model.Cards;

/**
 * this class is used to represent the cards
 */
public class Card{
    private CardColor color;
    private int sketch;

    public Card(CardColor color){
        this.color=color;
        this.sketch=(int)Math.random()*3;
    }

    public CardColor getColor(){
        return this.color;
    }

    public int getSketch(){
        return this.sketch;
    }

    @Override
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
    @Override
    public boolean equals (Object obj){ //make possible to insert card in hashset
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Card other = (Card) obj;
        return this.getColor() == other.getColor();
    }

}
