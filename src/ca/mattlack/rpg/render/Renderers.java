package ca.mattlack.rpg.render;

import ca.mattlack.rpg.render.renderers.*;

// Just here for organizational purposes.
public class Renderers {

    // These are in order of rendering priority.
    public static final Renderer BLOCK = new BlockRenderer().register();
    public static final Renderer ENTITY = new EntityRenderer().register();
    public static final Renderer DIALOG = new DialogRenderer().register();
    public static final Renderer WORLD_OBJECT = new WorldObjectRenderer().register();
    public static final Renderer MINI_MAP = new MiniMapRenderer().register();
    public static final Renderer GUI = new GuiRenderer().register();
    public static final Renderer OBJECTIVE = new ObjectiveRenderer().register();
    public static final Renderer INVENTORY = new HotbarRenderer().register();
    public static final Renderer CREDITS = new CreditRenderer().register();

}
