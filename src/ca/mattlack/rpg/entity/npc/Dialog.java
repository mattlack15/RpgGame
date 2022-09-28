package ca.mattlack.rpg.entity.npc;

import java.util.ArrayList;
import java.util.List;

public class Dialog {

    // List of dialog elements.
    private List<DialogElement> elements = new ArrayList<>();

    // The current element being used.
    private int currentElement = 0;

    public List<DialogElement> getElements() {
        return elements;
    }

    public Dialog addElement(DialogElement element) {
        elements.add(element);
        return this;
    }

    public int getCurrentElementIndex() {
        return currentElement;
    }

    public DialogElement getCurrentElement() {

        // Bounds check.
        if (currentElement < 0 || currentElement >= elements.size())
        {
            return null;
        }

        return elements.get(currentElement);
    }

    public void setCurrentElement(int currentElement) {
        // Perform a bounds check and then set the current element.
        this.currentElement = Math.max(0, Math.min(currentElement, elements.size() - 1));
    }

    /**
     * Increment the current element.
     */
    public void nextElement() {
        currentElement = Math.min(currentElement + 1, elements.size() - 1);
    }

    /**
     * Decrement the current element.
     */
    public void previousElement() {
        currentElement = Math.max(currentElement - 1, 0);
    }

}
