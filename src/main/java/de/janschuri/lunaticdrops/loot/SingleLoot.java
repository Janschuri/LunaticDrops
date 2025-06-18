package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.Utils;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleLoot extends Loot {

    private ItemStack drop;
    private int minAmount;
    private int maxAmount;

    public SingleLoot() {}

    public SingleLoot(
            Loot loot,
            ItemStack drop,
            int minAmount,
            int maxAmount
    ) {
        super(loot);
        this.drop = drop;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public SingleLoot(
            double chance,
            boolean active,
            ItemStack drop,
            int minAmount,
            int maxAmount,
            List<LootFlag> flags
    ) {
        super(chance, active, flags);
        this.drop = drop;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public SingleLoot(
            ItemStack drop,
            String chanceEquation,
            boolean active,
            int minAmount,
            int maxAmount,
            List<LootFlag> flags
    ) {
        super(chanceEquation, active, flags);
        this.drop = drop;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public ItemStack getDisplayItem() {
        return drop.clone();
    }

    @Override
    public List<ItemStack> getDrops(int bonusRolls, List<LootFlag> flags) {

        if (!isActive()) {
            return new ArrayList<>();
        }

        if (!hasFlag(LootFlag.DROP_ONLY_TO_PLAYER) && flags.contains(LootFlag.DROP_ONLY_TO_PLAYER)) {
            return new ArrayList<>();
        }

        if (!hasFlag(LootFlag.DROP_WITH_SILK_TOUCH) && flags.contains(LootFlag.DROP_WITH_SILK_TOUCH)) {
            return new ArrayList<>();
        }

        if (hasFlag(LootFlag.ONLY_FULL_GROWN) && flags.contains(LootFlag.ONLY_FULL_GROWN)) {
            return new ArrayList<>();
        }

        int amount = minAmount + (int) (Math.random() * (maxAmount - minAmount + 1));

        if (hasFlag(LootFlag.APPLY_FORTUNE) && flags.contains(LootFlag.APPLY_FORTUNE)) {
            for (int i = 0; i < bonusRolls; i++) {
                if (!Utils.isLucky(getChance())) {
                    continue;
                }
                amount += minAmount + (int) (Math.random() * (maxAmount - minAmount + 1));
            }
        }

        if (hasFlag(LootFlag.APPLY_LOOTING) && flags.contains(LootFlag.APPLY_LOOTING)) {
            for (int i = 0; i < bonusRolls; i++) {
                if (!Utils.isLucky(getChance())) {
                    continue;
                }
                amount += minAmount + (int) (Math.random() * (maxAmount - minAmount + 1));
            }
        }

        if (hasFlag(LootFlag.FORCE_MAX_AMOUNT)) {
            if (amount > maxAmount) {
                amount = maxAmount;
            }
        }

        ItemStack item = drop.clone();
        item.setAmount(amount);
        return List.of(item);
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    @Override
    public Map<String, Object> toMap() {
        List<String> flagStrings = new ArrayList<>();
        for (LootFlag flag : getFlags()) {
            flagStrings.add(flag.name());
        }

        Map<String, Object> map = new HashMap<>(super.toMap());
        map.put("type", "single");
        map.put("drop", ItemStackUtils.itemStackToMap(drop));
        map.put("minAmount", minAmount);
        map.put("maxAmount", maxAmount);

        return map;
    }

    public SingleLoot fromMap(Map<String, Object> map) {
        try {
            Object dropObj = map.get("drop");
            if (!(dropObj instanceof Map dropMap)) {
                Logger.errorLog("Drop is not a valid ItemStack map: " + dropObj);
                return null;
            }

            Loot loot = super.fromMap(map);
            ItemStack drop = ItemStackUtils.mapToItemStack(dropMap);
            Integer minAmount = Integer.parseInt(map.get("minAmount").toString());
            Integer maxAmount = Integer.parseInt(map.get("maxAmount").toString());

            return new SingleLoot(
                    loot,
                    drop,
                    minAmount > 0 ? minAmount : 1,
                    maxAmount > 0 ? maxAmount : 1
            );
        } catch (Exception e) {
            Logger.errorLog("Error loading SingleLoot: " + e.getMessage());
            return new SingleLoot();
        }
    }
}
