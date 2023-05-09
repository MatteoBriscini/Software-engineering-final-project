package it.polimi.ingsw.shared;


public enum TextColor {
    LIGHT_BLUE("\u001B[36m"),
    YELLOW("\u001B[33m"),
    DEFAULT("\u001B[0m");
    private String code;
    TextColor (String code){
        this.code=code;
    }
    public String get() {
        return code;
    }

}
