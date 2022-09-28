package ca.mattlack.rpg.item;

import ca.mattlack.rpg.util.Serializer;

import java.util.List;

public class Inventory
{

    // The array of items that this inventory will use to store references to the items it holds.
    private ItemStack[] items;

    public Inventory(int size)
    {
        items = new ItemStack[size];
    }

    public ItemStack[] getItems()
    {
        return items;
    }

    /**
     * Sets the item at the specified index to the specified item.
     *
     * @param index The index of the item to set.
     * @param item  The item to set.
     */
    public void setItem(int index, ItemStack item)
    {
        items[index] = item;
    }

    /**
     * Adds the specified item to the inventory.
     *
     * @param item The item to add.
     */
    public void addItem(ItemStack item)
    {
        // Look for items of the same type and add to them.
        for (int i = 0; i < items.length; i++)
        {
            if (items[i] != null && items[i].getItemType() == item.getItemType())
            {
                // We've found a matching item, so add to the item stack's quantity field.
                items[i].setQuantity(items[i].getQuantity() + item.getQuantity());
                return;
            }
        }

        // Look for empty slots and add to them.
        for (int i = 0; i < items.length; i++)
        {
            if (items[i] == null)
            {
                // Found an empty slot, so add the item here.
                items[i] = item;
                return;
            }
        }
    }

    /**
     * Removes the specified amount of items from the inventory.
     *
     * @param type   The type of item to remove.
     * @param amount The amount of items to remove.
     * @return Whether the removal was successful.
     */
    public boolean remove(ItemType type, int amount)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (items[i] != null && items[i].getItemType() == type)
            {
                // Check if the item stack contains more items than we want to remove.
                if (items[i].getQuantity() >= amount)
                {
                    // If so, then set remove that much from the stack.
                    items[i].setQuantity(items[i].getQuantity() - amount);

                    // Check if the amount of items left is 0, if so, set it to null.
                    if (items[i].getQuantity() == 0)
                    {
                        items[i] = null;
                    }

                    // Return true as we were able to remove all the items that we needed to.
                    return true;
                } else
                {
                    // This stack contains less than we want to remove.
                    // Subtract the amount in the stack from the amount we
                    // need to remove, and then completely remove the item stack
                    // by setting it to null.
                    amount -= items[i].getQuantity();
                    items[i] = null;
                }
            }
        }
        return false;
    }

    public ItemStack getItem(int index)
    {
        return items[index];
    }

    public int getSize()
    {
        return items.length;
    }

    /**
     * Serialize this inventory.
     */
    public Serializer serialize()
    {

        Serializer serializer = new Serializer();

        int version = 0;
        serializer.writeInt(version); // Write the version.

        serializer.writeInt(items.length); // Write the size of the inventory.
        for (int i = 0; i < items.length; i++)
        {
            if (items[i] != null) // If there is an item in this slot.
            {
                serializer.writeBoolean(true); // Write a boolean indicating that there is an item to be read.
                serializer.writeString(items[i].getItemType().name()); // Write the name of the type of the item.
                serializer.writeInt(items[i].getQuantity()); // Write the quantity of the item stack.
            } else
            {
                serializer.writeBoolean(false); // There is no item in this slot so write a boolean indicating as such.
            }
        }
        return serializer;
    }

    public static Inventory deserialize(Serializer serializer)
    {
        int version = serializer.readInt(); // Read the version.

        int size = serializer.readInt(); // Read the size of the inventory.
        Inventory inventory = new Inventory(size); // Make an inventory with that size.

        for (int i = 0; i < size; i++) // Go through each slot.
        {
            if (serializer.readBoolean()) // If there is an item in this slot.
            {
                String name = serializer.readString(); // Read the name of the type.
                int quantity = serializer.readInt(); // Read the quantity of the item stack.
                try
                {
                    inventory.items[i] = new ItemStack(ItemType.valueOf(name), quantity); // Try and get the item type and create an item stack from them and assign it to the correct slot.
                } catch (IllegalArgumentException e)
                {
                    System.out.println("Unknown item type: " + name); // If the name of the item type was invalid and no type was found, print a message saying as such.
                }
            }
        }

        return inventory;
    }

    /**
     * Returns the number of items of the given type in the inventory.
     *
     * @param itemType the type of item to count
     * @return the number of items of the given type in the inventory
     */
    public int count(ItemType itemType)
    {
        // Accumulator.
        int count = 0;
        for (ItemStack item : items)
        {
            if (item != null && item.getItemType() == itemType)
            { // The item matches the type so add the quantity to the accumulator.
                count += item.getQuantity();
            }
        }

        // Return the accumulator.
        return count;
    }
}
