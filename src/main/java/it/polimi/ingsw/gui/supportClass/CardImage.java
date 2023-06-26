package it.polimi.ingsw.gui.supportClass;

import it.polimi.ingsw.shared.Cards.CardColor;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class CardImage {
    private static final String[] blue = {"Cornici1.1.png", "Cornici1.2.png", "Cornici1.3.png"};
    private static final String[] green = {"Gatti1.1.png","Gatti1.2.png", "Gatti1.3.png"};
    private static final String[] yellow = {"Giochi1.1.png", "Giochi1.2.png", "Giochi1.3.png"};
    private static final String[] white = {"Libri1.1.png", "Libri1.2.png", "Libri1.3.png"};
    private static final String[] pink = {"Piante1.1.png", "Piante1.2.png","Piante1.3.png"};
    private static final String[] lightBlue = {"Trofei1.1.png", "Trofei1.2.png","Trofei1.3.png"};
    public static String getImgName(int sketch, CardColor color){
        if(sketch >2)sketch=2;
        return switch (color) {
            case BLUE -> blue[sketch];
            case GREEN -> green[sketch];
            case YELLOW -> yellow[sketch];
            case WHITE -> white[sketch];
            case PINK -> pink[sketch];
            default -> lightBlue[sketch];
        };
    }


}
