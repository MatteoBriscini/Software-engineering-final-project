package it.polimi.ingsw.gui.supportClass;

/**
 * this class convert private goal data into chat object
 */
public class Message {
    String text;
    MessageTipe msgType;

    public Message (String text, MessageTipe msgType){
        this.text=text;
        this.msgType = msgType;
    }

    public String getText() {
        return text;
    }

    public MessageTipe getMsgType() {
        return msgType;
    }
}
