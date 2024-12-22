package de.janschuri.lunaticdrops.loot;

import org.bukkit.inventory.ItemStack;

public class Loot {

    private final ItemStack item;
    private final float chance;
    private final boolean active;
    private final int minAmount;
    private final int maxAmount;
    private final boolean applyFortune;
    private final boolean dropWithSilkTouch;

    // if true, vanilla drops will be erased, when this loot is dropped
    private boolean eraseVanillaDrops;

    public Loot(
            ItemStack item,
            float chance,
            boolean active,
            int minAmount,
            int maxAmount,
            boolean applyFortune,
            boolean dropWithSilkTouch,
            boolean eraseVanillaDrops
    ) {
        this.item = item;
        this.chance = chance;
        this.active = active;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.applyFortune = applyFortune;
        this.dropWithSilkTouch = dropWithSilkTouch;
        this.eraseVanillaDrops = eraseVanillaDrops;
    }

    public ItemStack getItem() {
        return item;
    }

    public float getChance() {
        return chance;
    }

    public boolean isActive() {
        return active;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public boolean getApplyFortune() {
        return applyFortune;
    }

    public boolean getDropWithSilkTouch() {
        return dropWithSilkTouch;
    }

    public boolean getEraseVanillaDrops() {
        return eraseVanillaDrops;
    }
}
