package ca.mattlack.rpg.entity;

import ca.mattlack.rpg.world.World;

import java.util.UUID;

/**
 * Represents an entity with a health bar.
 * Health is not actually used in the game.
 * I was planning on using it but time became
 * a constraint. If I continue to work on this
 * game after I submit it as my culminating project,
 * (which I may or may not, but I'm definitely uploading
 * it to my github), I will probably make enemies and make
 * use of health in that way.
 */
public abstract class LivingEntity extends Entity {

    private double maxHealth = 20;
    private double health = maxHealth * 0.8;

    public LivingEntity(World world) {
        super(world);
    }

    public LivingEntity(World world, UUID id) {
        super(world, id);
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }
}
