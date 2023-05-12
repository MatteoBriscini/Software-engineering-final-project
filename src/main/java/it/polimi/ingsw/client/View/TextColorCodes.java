package it.polimi.ingsw.client.View;

public enum TextColorCodes {

    BLUE("\033[0;34m"),    // BLUE
    GREEN("\033[0;32m"),   // GREEN
    LIGHTBLUE("\033[0;36m"),    // CYAN
    PINK("\033[0;35m"),  // PURPLE

    WHITE("\033[0;37m"),   // WHITE
    YELLOW("\033[0;33m"),  // YELLOW
    EMPTY("\033[0;30m");   // BLACK


    private String code;


    public String get() {
        return code;
    }

    public void set(String code){
        this.code=code;
    }

    TextColorCodes(String code){
        this.code=code;
    }
}
