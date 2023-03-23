package it.polimi.ingsw.SupportClasses;

/**
 * RecursiveUsed used method have to return this class
 */

public class RecursiveUsedSupport {
    private final boolean[][] alreadyUsed;
    private final int elementCombo;

    /**
     * constructor:
     * @param alreadyUsed boolean matrix to take track of just used card
     * @param elementCombo total amount of card of the same color in a single shape
     */
    public RecursiveUsedSupport(boolean[][] alreadyUsed, int elementCombo){
        this.alreadyUsed = alreadyUsed;
        this.elementCombo = elementCombo;
    }

    public boolean[][] getAlreadyUsed() {
        return alreadyUsed;
    }

    public int getElementCombo() {
        return elementCombo;
    }
}
