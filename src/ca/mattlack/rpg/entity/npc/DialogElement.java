package ca.mattlack.rpg.entity.npc;

public class DialogElement {

    private String text;
    private String response;

    public DialogElement(String text, String response) {
        this.text = text;
        this.response = response;
    }

    public String getText() {
        return text;
    }

    public String getResponse() {
        return response;
    }

}