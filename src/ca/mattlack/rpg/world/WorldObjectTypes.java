package ca.mattlack.rpg.world;

import ca.mattlack.rpg.world.worldobjects.WorldHut;
import ca.mattlack.rpg.world.worldobjects.WorldSmallTree;
import ca.mattlack.rpg.world.worldobjects.WorldTree;

public class WorldObjectTypes
{

    public static final WorldObjectType TREE = new WorldObjectType("tree", WorldTree::deserialize, WorldTree::serialize).register();
    public static final WorldObjectType SMALL_TREE = new WorldObjectType("small_tree", WorldSmallTree::deserialize, WorldSmallTree::serialize).register();
    public static final WorldObjectType HUT = new WorldObjectType("hut", WorldHut::deserialize, WorldHut::serialize).register();

    public static void loadClassByFunc()
    {
        // Literally do nothing just make sure that java loads this class and therefore initializes the world object types.
    }

}