package ca.mattlack.rpg.render.renderers;

import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.render.PlayerCamera;
import ca.mattlack.rpg.render.Renderer;
import ca.mattlack.rpg.world.World;
import ca.mattlack.rpg.world.WorldMap;
import ca.mattlack.rpg.world.WorldObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldObjectRenderer extends Renderer
{

    public WorldObjectRenderer()
    {
        super("world_object");
    }

    @Override
    public void render(Graphics2D screen, PlayerCamera camera)
    {
        World world = camera.getPlayer().getWorld();
        WorldMap map = world.getMap();

        List<Map.Entry<IntVector2D, WorldObject>> entryList = new ArrayList<>(map.getWorldObjects().entrySet());

        // Sort from highest y-value to lowest y-value.
        entryList.sort((o1, o2) -> o2.getValue().getPosition().getY() - o1.getValue().getPosition().getY());

        // Go through all world objects in the map and draw them.
        for (Map.Entry<IntVector2D, WorldObject> entry : entryList)
        {
            Graphics2D graphics2D = (Graphics2D) screen.create(); // Create a new graphics object to draw on.
            camera.transform(graphics2D, entry.getValue().getPosition().toVector2D()); // Transform the graphics object to the world object's position.
            entry.getValue().draw(graphics2D); // Draw the world object.
        }
    }

    @Override
    public int priority()
    {
        return 3;
    }
}
