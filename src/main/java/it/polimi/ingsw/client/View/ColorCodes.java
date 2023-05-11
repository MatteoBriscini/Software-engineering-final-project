package it.polimi.ingsw.client.View;

public enum ColorCodes {

    BLUE("\033[44m"),   // BLUE
    GREEN("\033[42m"),  // GREEN
    LIGHTBLUE("\033[46m"),   // CYAN
    PURPLE("\033[45m"), // PURPLE
    WHITE("\033[47m"),  // WHITE
    YELLOW("\033[43m"), // YELLOW
    EMPTY("\033[40m");  // BLACK







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
