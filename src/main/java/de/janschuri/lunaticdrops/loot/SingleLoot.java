package de.janschuri.lunaticdrops.loot;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class SingleLoot implements Loot {

    private final int amount;
    private final float chance;
    private final ItemStack itemStack;

    public SingleLoot(ItemStack itemStack, int amount, float chance) {
        this.itemStack = itemStack;
        this.amount = amount;
        this.chance = chance;
    }

    @Override
    public List<ItemStack> getDrops() {
        return List.of(itemStack);
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public float getChance() {
        return chance;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of(
                "drop", itemStack,
                "amount", amount,
                "chance", chance
        );
    }

    public static SingleLoot fromMap(Map<String, Object> map) {
        return null;
    }
}
