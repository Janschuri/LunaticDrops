package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils.itemStackToMap;
import static de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils.mapToItemStack;

public class PandaEat extends CustomDrop {

    private final ItemStack eatenItem;
    private final boolean matchNBT;
    private final String name;

    public PandaEat(@NotNull String name, @NotNull ItemStack drop, @NotNull Float chance, @NotNull Boolean active, @NotNull ItemStack eatenItem, @NotNull Boolean matchNBT) {
        super(drop, chance, active);
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
        return name;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of(
                "name", name,
                "drop", itemStackToMap(drop),
                "chance", chance,
                "active", active,
                "eatenItem", itemStackToMap(eatenItem),
                "matchNBT", matchNBT
        );
    }

    @Override
    protected DropType getDropType() {
        return DropType.PANDA_EAT;
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
