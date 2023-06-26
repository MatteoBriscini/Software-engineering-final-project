package it.polimi.ingsw.gui.supportClass;

import it.polimi.ingsw.shared.Cards.CardColor;
import javafx.scene.image.Image;

public class Title {
    Image image;
    CardColor color;
    int sketch;
    public Title(CardColor color, int sketch){
        this.color = color;
        this.sketch = sketch;
    }

    public Title(Image image, CardColor color, int sketch){
        this.image = image;
        this.color = color;
        this.sketch = sketch;
    }

    public CardColor getColor() {
        return color;
    }

    public Image getImage() {
        return image;
    }

    public int getSketch() {
        return sketch;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Title other = (Title) obj;
        return  this.getColor()== other.getColor() && this.getSketch()== other.getSketch();
    }
}
