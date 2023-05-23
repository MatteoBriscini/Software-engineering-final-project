package it.polimi.ingsw.shared;


public enum TextColor {

    BLUE("\033[38;2;0;0;255m"),   // BLUE
    GREEN("\033[38;2;0;255;0m"),  // GREEN
    LIGHTBLUE("\033[38;2;0;255;255m"),   // CYAN
    PINK("\033[38;2;255;102;255m"), // PURPLE
    WHITE("\033[38;2;255;255;255m"),  // WHITE
    YELLOW("\033[38;2;255;255;0m"), // YELLOW
    EMPTY("\033[0;0;0m"),   // BLACK
    RED("\u001B[31m"),
    DEFAULT("\u001B[0m");
    private String code;
    TextColor (String code){
        this.code=code;
    }
    public String get() {
        return code;
    }
}
