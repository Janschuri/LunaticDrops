package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class CustomDrop {

    private final float chance;
    private final Random random = new Random();
    private final ItemStack drop;
    private final boolean active;

    public CustomDrop(ItemStack drop, float chance, boolean active) {
        this.chance = chance;
        this.drop = drop;
        this.active = active;
    }

    public double getChance() {
        return chance;
    }

    public boolean isLucky() {
        float randomValue = random.nextFloat(0, 1); // Generates a float between 0.0 (inclusive) and 1.0 (exclusive)
        Logger.debugLog("Random value: " + randomValue + " Chance: " + chance);
        return randomValue <= chance;
    }

    public boolean isActive() {
        return active;
    }

    public ItemStack getDrop() {
        return drop;
    }
}
