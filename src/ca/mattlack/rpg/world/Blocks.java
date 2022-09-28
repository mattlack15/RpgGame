package ca.mattlack.rpg.world;

public class Blocks {
    public static final Block GRASS = new Block("grass", false, "grass").register();
    public static final Block DIRT = new Block("dirt", false, "dirt").register();
    public static final Block AIR = new Block("air", false, "").register();
    public static final Block PATH = new Block("path", false, "path").register();
    public static final Block LEAVES = new Block("leaves", true, "leaves").register();
    public static final Block DARK_LEAVES = new Block("dark_leaves", true, "dark-leaves").register();
    public static final Block WOOD = new Block("wood", false, "wood").register();
    public static final Block STONE = new Block("stone", true, "stone").register();
    public static final Block ROOF = new Block("roof", true, "roof").register();
    public static final Block WATER = new Block("water", false, "water").register();
    public static final Block STONE_FLOOR = new Block("stone_floor", false, "stone").register();
    public static final Block PINK_CRYSTALS = new Block("pink_crystals", false, "pink-crystals").register();
    public static final Block BLUE_CRYSTALS = new Block("blue_crystals", false, "blue-crystals").register();
}
