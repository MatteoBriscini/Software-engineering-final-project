package it.polimi.ingsw.gui.supportClass;

import it.polimi.ingsw.shared.Cards.CardColor;
import junit.framework.TestCase;

import java.util.ArrayList;

public class TitleTest extends TestCase {
    public  void test(){
        ArrayList<Title> titles = new ArrayList<>();
        titles.add(new Title(CardColor.BLUE, 0));
        assertTrue(titles.contains(new Title(CardColor.BLUE, 0)));
        assertFalse(titles.contains(new Title(CardColor.BLUE, 2)));
        assertFalse(titles.contains(new Title(CardColor.GREEN, 0)));
    }
}