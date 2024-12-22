package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.Logger;
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
                "type", "single",
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

        Logger.debugLog("SingleLoot.fromMap: " + map);

        try {
            ItemStack drop = ItemStackUtils.mapToItemStack((Map<String, Object>) map.get("drop"));
            float chance = Float.parseFloat(map.get("chance").toString());
            boolean active = Boolean.parseBoolean(map.get("active").toString());
            int minAmount = Integer.parseInt(map.get("minAmount").toString());
            int maxAmount = Integer.parseInt(map.get("maxAmount").toString());
            boolean applyFortune = Boolean.parseBoolean(map.get("applyFortune").toString());
            boolean dropWithSilkTouch = Boolean.parseBoolean(map.get("dropWithSilkTouch").toString());
            boolean eraseVanillaDrops = Boolean.parseBoolean(map.get("eraseVanillaDrops").toString());

            return new SingleLoot(
                    drop,
                    chance,
                    active,
                    minAmount,
                    maxAmount,
                    applyFortune,
                    dropWithSilkTouch,
                    eraseVanillaDrops
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
