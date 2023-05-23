package it.polimi.ingsw.client.View;

public enum ColorCodes {

    BLUE("\033[48;2;0;0;255m"),   // BLUE
    GREEN("\033[48;2;0;255;0m"),  // GREEN
    LIGHTBLUE("\033[48;2;0;255;255m"),   // CYAN
    PINK("\033[48;2;255;102;255m"), // PURPLE
    WHITE("\033[48;2;255;255;255m"),  // WHITE
    YELLOW("\033[48;2;255;255;0m"), // YELLOW
    BROWN("\033[48;2;160;82;45m"),
    EMPTY("\033[48;2;0;0;0m"),  // BLACK
    DEFAULT("\033[0m");








    private String code;


    public String get() {
        return code;
    }

    public void set(String code){
        this.code=code;
    }

    ColorCodes(String code){
        this.code=code;
    }
}
