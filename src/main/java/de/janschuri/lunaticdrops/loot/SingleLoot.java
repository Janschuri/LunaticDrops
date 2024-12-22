package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class SingleLoot implements Loot {

    private final ItemStack drop;
    private final float chance;
    private final boolean active;
    private final int minAmount;
    private final int maxAmount;
    private final boolean applyFortune;
    private final boolean dropWithSilkTouch;

    // if true, vanilla drops will be erased, when this loot is dropped
    private final boolean eraseVanillaDrops;

    public SingleLoot(
            ItemStack drop,
            float chance,
            boolean active,
            int minAmount,
            int maxAmount,
            boolean applyFortune,
            boolean dropWithSilkTouch,
            boolean eraseVanillaDrops
    ) {
        this.drop = drop;
        this.chance = chance;
        this.active = active;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.applyFortune = applyFortune;
        this.dropWithSilkTouch = dropWithSilkTouch;
        this.eraseVanillaDrops = eraseVanillaDrops;
    }

    @Override
    public ItemStack getItem() {
        return drop.clone();
    }

    @Override
    public boolean isEraseVanillaDrops() {
        return false;
    }

    @Override
    public List<ItemStack> getDrops() {
        return List.of(drop);
    }

    public float getChance() {
        return chance;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of(
                "drop", ItemStackUtils.itemStackToMap(drop),
                "chance", chance,
                "active", active,
                "minAmount", minAmount,
                "maxAmount", maxAmount,
                "applyFortune", applyFortune,
                "dropWithSilkTouch", dropWithSilkTouch,
                "eraseVanillaDrops", eraseVanillaDrops
        );
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

    public static SingleLoot fromMap(Map<String, Object> map) {

        ItemStack drop = ItemStackUtils.mapToItemStack((Map<String, Object>) map.get("drop"));

        return new SingleLoot(
                drop,
                (float) map.get("chance"),
                (boolean) map.get("active"),
                (int) map.get("minAmount"),
                (int) map.get("maxAmount"),
                (boolean) map.get("applyFortune"),
                (boolean) map.get("dropWithSilkTouch"),
                (boolean) map.get("eraseVanillaDrops")
        );
    }
}
