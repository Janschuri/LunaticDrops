package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.Utils;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleLoot implements Loot {

    private final ItemStack drop;
    private final float chance;
    private final boolean active;
    private final int minAmount;
    private final int maxAmount;
    private List<LootFlag> flags;

    public SingleLoot(
            ItemStack drop,
            float chance,
            boolean active,
            int minAmount,
            int maxAmount,
            List<LootFlag> flags
    ) {
        this.drop = drop;
        this.chance = chance;
        this.active = active;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.flags = flags;
    }

    @Override
    public ItemStack getItem() {
        return drop.clone();
    }

    @Override
    public List<ItemStack> getDrops(List<LootFlag> flags, int bonusRolls) {

        if (!this.active) {
            return new ArrayList<>();
        }

        if (!isDropOnlyToPlayer() && flags.contains(LootFlag.DROP_ONLY_TO_PLAYER)) {
            return new ArrayList<>();
        }

        if (!isDropWithSilkTouch() && flags.contains(LootFlag.DROP_WITH_SILK_TOUCH)) {
            return new ArrayList<>();
        }

        int amount = minAmount + (int) (Math.random() * (maxAmount - minAmount + 1));

        if (isApplyFortune() && flags.contains(LootFlag.APPLY_FORTUNE)) {
            for (int i = 0; i < bonusRolls; i++) {
                if (!Utils.isLucky(chance)) {
                    continue;
                }
                amount += minAmount + (int) (Math.random() * (maxAmount - minAmount + 1));
            }
        }

        if (isApplyLooting() && flags.contains(LootFlag.APPLY_LOOTING)) {
            for (int i = 0; i < bonusRolls; i++) {
                if (!Utils.isLucky(chance)) {
                    continue;
                }
                amount += minAmount + (int) (Math.random() * (maxAmount - minAmount + 1));
            }
        }

        if (isForceMaxAmount()) {
            if (amount > maxAmount) {
                amount = maxAmount;
            }
        }

        ItemStack item = drop.clone();
        item.setAmount(amount);

        return List.of(item);
    }

    public float getChance() {
        return chance;
    }

    @Override
    public Map<String, Object> toMap() {
        List<String> flagStrings = new ArrayList<>();
        for (LootFlag flag : flags) {
            flagStrings.add(flag.name());
        }

        return Map.of(
                "type", "single",
                "drop", ItemStackUtils.itemStackToMap(drop),
                "chance", chance,
                "active", active,
                "minAmount", minAmount,
                "maxAmount", maxAmount,
                "flags", flagStrings
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

    public boolean isApplyFortune() {
        return flags.contains(LootFlag.APPLY_FORTUNE);
    }

    public boolean isApplyLooting() {
        return flags.contains(LootFlag.APPLY_LOOTING);
    }

    public boolean isDropWithSilkTouch() {
        return flags.contains(LootFlag.DROP_WITH_SILK_TOUCH);
    }

    @Override
    public boolean isEraseVanillaDrops() {
        return flags.contains(LootFlag.ERASE_VANILLA_DROPS);
    }

    public List<LootFlag> getFlags() {
        return flags;
    }

    public boolean isDropOnlyToPlayer() {
        return flags.contains(LootFlag.ERASE_VANILLA_DROPS);
    }

    public boolean isForceMaxAmount() {
        return flags.contains(LootFlag.FORCE_MAX_AMOUNT);
    }

    public static SingleLoot fromMap(Map<String, Object> map) {

        Logger.debugLog("SingleLoot.fromMap: " + map);

        try {
            ItemStack drop = ItemStackUtils.mapToItemStack((Map<String, Object>) map.get("drop"));
            float chance = Float.parseFloat(map.get("chance").toString());
            boolean active = Boolean.parseBoolean(map.get("active").toString());
            int minAmount = Integer.parseInt(map.get("minAmount").toString());
            int maxAmount = Integer.parseInt(map.get("maxAmount").toString());

            List<LootFlag> flags = new ArrayList<>();

            try {
                List<String> flagStrings = (List<String>) map.get("flags");
                for (String flagString : flagStrings) {

                    try {
                        LootFlag flag = LootFlag.valueOf(flagString);
                        flags.add(flag);
                    } catch (IllegalArgumentException e) {
                        Logger.errorLog("Unknown flag: " + flagString);
                    }

                }
            } catch (Exception e) {
                Logger.errorLog("No flags found");
            }

            return new SingleLoot(
                    drop,
                    chance,
                    active,
                    minAmount,
                    maxAmount,
                    flags
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
