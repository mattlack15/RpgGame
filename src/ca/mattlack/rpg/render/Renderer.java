package ca.mattlack.rpg.render;

import ca.mattlack.rpg.util.Registry;

import java.awt.*;

public abstract class Renderer {

    public static final Registry<String, Renderer> REGISTRY = new Registry<>();

    private final String identifier;

    public Renderer(String identifier) {
        this.identifier = identifier;
    }

    public Renderer register() {
        return REGISTRY.register(this.identifier, this);
    }

    public abstract void render(Graphics2D screen, PlayerCamera camera);

    public abstract int priority();
}
