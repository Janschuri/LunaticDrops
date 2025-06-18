package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils.itemStackToMap;

public class DropPandaEat extends Drop {

    private final ItemStack eatenItem;
    private final boolean matchNBT;
    private final String name;

    public DropPandaEat(@NotNull String name, @NotNull List<Loot> loot, boolean active, @NotNull ItemStack eatenItem, boolean matchNBT) {
        super(loot, active);
        this.name = name;
        this.eatenItem = eatenItem;
        this.matchNBT = matchNBT;
    }

    public DropPandaEat(Drop drop, @NotNull String name, @NotNull ItemStack eatenItem, boolean matchNBT) {
        super(drop);
        this.name = name;
        this.eatenItem = eatenItem;
        this.matchNBT = matchNBT;
    }

    public boolean matchEatenItem(ItemStack item) {
        if (matchNBT) {
            Logger.debugLog("Matching NBT");
            return item.isSimilar(eatenItem);
        } else {
            Logger.debugLog("Not matching NBT");
            return item.getType() == eatenItem.getType();
        }
    }

    @Override
    public String getName() {
        Logger.debugLog("Returning name: " + name);
        return name;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("eatenItem", itemStackToMap(eatenItem));
        map.put("matchNBT", matchNBT);
        return map;
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.PANDA_EAT;
    }

    @Override
    public ItemStack getDisplayItem() {
        return eatenItem.clone();
    }

    public boolean isMatchNBT() {
        return matchNBT;
    }

    public ItemStack getEatenItem() {
        return eatenItem;
    }
}
