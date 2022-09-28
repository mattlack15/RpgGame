package ca.mattlack.rpg.item;

import java.util.Objects;


/**
 * Represents a stack of items. ei. an amount of items of the same type.
 */
public class ItemStack {

    private final ItemType itemType;
    private int quantity;

    public ItemStack(ItemType itemType, int quantity) {
        this.itemType = Objects.requireNonNull(itemType);
        this.quantity = quantity;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
