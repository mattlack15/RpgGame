package ca.mattlack.rpg.world;

import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.util.Serializer;

import java.util.HashMap;
import java.util.Map;

public class WorldMap {

    // An array of the types of all the blocks in this map.
    private int[][] blockTypes = new int[128][128];

    // A map of all the world objects in this map.
    private Map<IntVector2D, WorldObject> worldObjects = new HashMap<>();

    public int[][] getBlockTypes() {
        return blockTypes;
    }

    public Map<IntVector2D, WorldObject> getWorldObjects() {
        return worldObjects;
    }

    public WorldMap() {
    }

    public WorldMap(int width, int height) {
        blockTypes = new int[width][height]; // Create a new block type array with the given width and height.
    }

    /**
     * Returns the world object at the given position.
     *
     * @param position The position to get the world object at.
     * @return The world object at the given position.
     */
    public WorldObject getWorldObject(IntVector2D position) {
        // Return the world object at the given position.
        WorldObject object = worldObjects.get(position);
        if (object != null) {
            object.setPosition(position); // Make sure the position of the object is correct.
        }
        return object;
    }

    /**
     * Returns the world object at the given position.
     *
     * @param x The x coordinate of the position to get the world object at.
     * @param y The y coordinate of the position to get the world object at.
     * @return The world object at the given position.
     */
    public WorldObject getWorldObject(int x, int y) {
        return getWorldObject(new IntVector2D(x, y));
    }

    /**
     * Removes the world object at the given position.
     *
     * @param position The position to remove the world object at.
     */
    public void removeWorldObject(IntVector2D position) {
        worldObjects.remove(position);
    }

    /**
     * Sets the world object at the given position.
     *
     * @param position The position to set the world object at.
     * @param object   The world object to set.
     */
    public void setWorldObject(IntVector2D position, WorldObject object) {
        worldObjects.put(position, object);
        if (object != null) {
            object.setPosition(position);
        }
    }

    /**
     * Gets the block at the specified position.
     *
     * @param position The position.
     * @return The block at the specified position.
     */
    public Block getBlock(IntVector2D position) {
        if (!boundsCheck(position)) return null;

        return Block.REGISTRY.get(blockTypes[position.getX()][position.getY()]);
    }

    /**
     * Gets the block at the specified position.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The block at the specified position.
     */
    public Block getBlock(int x, int y) {
        if (!boundsCheck(x, y)) return null;
        return Block.REGISTRY.get(blockTypes[x][y]);
    }

    /**
     * Sets the block at the specified position.
     *
     * @param position The position.
     * @param block    The block.
     */
    public void setBlock(IntVector2D position, Block block) {
        if (!boundsCheck(position)) return;

        if (block == null) return;

        blockTypes[position.getX()][position.getY()] = block.getId();
    }

    /**
     * Checks if the given position is within the bounds of the map.
     *
     * @param position The position to check.
     * @return True if the position is within the bounds of the map, false otherwise.
     */
    public boolean boundsCheck(IntVector2D position) {
        return position.getX() >= 0 && position.getX() < blockTypes.length && position.getY() >= 0 && position.getY() < blockTypes[0].length;
    }

    /**
     * Checks if the given position is within the bounds of the map.
     *
     * @param x The x coordinate of the position to check.
     * @param y The y coordinate of the position to check.
     * @return True if the position is within the bounds of the map, false otherwise.
     */
    public boolean boundsCheck(int x, int y) {
        return x >= 0 && x < blockTypes.length && y >= 0 && y < blockTypes[0].length;
    }

    /**
     * Set the blocks in a rectangular area to a specific block.
     *
     * @param corner The corner of the rectangle of blocks to set.
     * @param width  The width of the rectangle of blocks to set.
     * @param height The height of the rectangle of blocks to set.
     * @param block  The block to set to.
     */
    public void setBlocks(IntVector2D corner, int width, int height, Block block) {
        for (int x = corner.getX(); x < corner.getX() + width; x++) {
            for (int y = corner.getY(); y < corner.getY() + height; y++) {
                setBlock(new IntVector2D(x, y), block);
            }
        }
    }

    /**
     * Sets the map.
     *
     * @param blockTypes the map
     */
    public void setMap(int[][] blockTypes) {
        this.blockTypes = blockTypes;
    }

    /**
     * Gets the width of the map.
     *
     * @return the width
     */
    public int getWidth() {
        return blockTypes.length;
    }

    /**
     * Gets the height of the map.
     *
     * @return the height
     */
    public int getHeight() {
        return blockTypes[0].length;
    }

    /**
     * Copies a rectangular region of the map.
     *
     * @param x      the x coordinate of the top left corner of the region
     * @param y      the y coordinate of the top left corner of the region
     * @param width  the width of the region
     * @param height the height of the region
     * @return a copy of the region
     */
    public WorldMap copy(int x, int y, int width, int height) {
        WorldMap map = new WorldMap();
        map.blockTypes = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map.blockTypes[i][j] = blockTypes[i + x][j + y];
            }
        }
        return map;
    }

    /**
     * Copies a rectangular region of the map.
     *
     * @param corner the top left corner of the region
     * @param size   the size of the region
     * @return a copy of the region
     */
    public WorldMap copy(IntVector2D corner, IntVector2D size) {
        return copy(corner.getX(), corner.getY(), size.getX(), size.getY());
    }

    /**
      Pastes a map into this map.
     *
     * @param map the map to paste
     * @param x   the x coordinate of the top left corner of the region to paste into
     * @param y   the y coordinate of the top left corner of the region to paste into
     */
    public void paste(WorldMap map, int x, int y) {
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                blockTypes[i + x][j + y] = map.blockTypes[i][j];
            }
        }
    }

    /**
      Pastes another map into this map.
     *
     * @param map      the map to paste
     * @param position the position to paste into
     */
    public void paste(WorldMap map, IntVector2D position) {
        paste(map, position.getX(), position.getY());
    }

    /**
     * Serializes this map into a serializer ready to be written to a file.
     */
    public Serializer serialize() {

        Serializer serializer = new Serializer(); // Create a new serializer.

        final int VERSION = 1;

        serializer.writeInt(VERSION); // Write the version.

        serializer.writeInt(getWidth()); // Write the width.
        serializer.writeInt(getHeight()); // Write the height.

        for (int x = 0; x < getWidth(); x++) { // Write the block types.
            for (int y = 0; y < getHeight(); y++) {
                serializer.writeInt(blockTypes[x][y]);
            }
        }

        serializer.writeInt(worldObjects.size()); // Write the number of world objects.
        for (Map.Entry<IntVector2D, WorldObject> entry : worldObjects.entrySet()) { // For each world object.

            IntVector2D position = entry.getKey(); // Get the position.
            WorldObject object = entry.getValue(); // Get the object.

            object.setPosition(position); // Make sure the object has the correct position.

            WorldObjectType type = object.getType(); // Get the type.
            if (type == null) continue; // If the type is null, skip this object.

            Serializer serializedWorldObject = type.getSerializer().apply(object); // Serialize the object.
            serializer.writeString(type.getIdentifier()); // Write the type identifier.
            serializer.writeSerializer(object.getPosition().serialize()); // Write the position.
            serializer.writeSerializer(serializedWorldObject); // Write the serialized object.
        }

        return serializer;
    }

    /**
     * Deserializes a map from a serializer.

     */
    public static WorldMap deserialize(Serializer serializer) {
        WorldMap map = new WorldMap();

        int version = serializer.readInt(); // Read the version.

        int width = serializer.readInt(); // Read the width.
        int height = serializer.readInt(); // Read the height.

        map.blockTypes = new int[width][height]; // Create the block types array with the correct size.

        for (int x = 0; x < width; x++) { // Read the block types.
            for (int y = 0; y < height; y++) {
                map.blockTypes[x][y] = serializer.readInt(); // Read the block type.
            }
        }

        // World objects.
        int objectCount = serializer.readInt(); // Read the number of world objects.
        for (int i = 0; i < objectCount; i++) { // For each world object.

            String identifier = serializer.readString(); // Read the type identifier.
            IntVector2D position = IntVector2D.deserialize(serializer.readSerializer()); // Read the position.

            WorldObjectType type = WorldObjectType.REGISTRY.get(identifier); // Get the type.

            if (type == null) { // If the type is null, skip this object and output an error message.
                System.err.println("Unknown world object type: " + identifier);
                continue;
            }

            WorldObject object = type.getDeserializer().apply(serializer.readSerializer()); // Deserialize the object using the type's deserializer.
            map.setWorldObject(position, object); // Put the world object on the map.
        }

        return map;
    }

    public IntVector2D getDimensions() {
        return new IntVector2D(getWidth(), getHeight());
    }
}
