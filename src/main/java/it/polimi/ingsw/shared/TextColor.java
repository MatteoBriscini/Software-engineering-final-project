package it.polimi.ingsw.shared;


public enum TextColor {
    LIGHTBLUE("\033[0;36m"),    // CYAN
    YELLOW("\033[0;33m"),  // YELLOW
    BLUE("\033[0;34m"),    // BLUE
    PINK("\033[0;35m"),  // PURPLE
    GREEN("\033[0;32m"),   // GREEN
    WHITE("\033[0;37m"),   // WHITE
    EMPTY("\033[0;30m"),   // BLACK
    DEFAULT("\u001B[0m");
    private String code;
    TextColor (String code){
        this.code=code;
    }
    public String get() {
        return code;
    }
}
