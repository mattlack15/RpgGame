package ca.mattlack.rpg.world;

import ca.mattlack.rpg.math.IntVector2D;

/**
 * This class is used to represent a block in a world with a position and world, not just a type.
 */
public class WrappedBlock
{

    private final IntVector2D position; // The position of the block in the world.
    private final World world; // The world the block is in.

    public WrappedBlock(IntVector2D position, World world)
    {
        this.position = position;
        this.world = world;
    }

    public IntVector2D getPosition()
    {
        return position;
    }

    public World getWorld()
    {
        return world;
    }

    public Block getType()
    {
        return world.getMap().getBlock(position);
    }

    public void setType(Block block)
    {
        world.getMap().setBlock(position, block);
    }

}
