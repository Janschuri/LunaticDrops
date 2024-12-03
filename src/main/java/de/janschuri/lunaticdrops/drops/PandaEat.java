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

    public PandaEat(@NotNull String name, @NotNull ItemStack drop, @NotNull Float chance, @NotNull Boolean active, @NotNull ItemStack eatenItem, @NotNull Boolean matchNBT) {
        super(name, drop, chance, active);
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
    public Map<String, Object> toMap() {
        Map<String,Object> map = Map.of(
                "name", name,
                "drop", itemStackToMap(drop),
                "chance", chance,
                "active", active,
                "eatenItem", itemStackToMap(eatenItem),
                "matchNBT", matchNBT
        );

        Map<String, Object> test = (Map<String, Object>) map.get("drop");

        ItemStack item = mapToItemStack(test);

        Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().addItem(item));

        return map;
    }

    @Override
    protected DropType getDropType() {
        return DropType.PANDA_EAT;
    }

    public boolean isMatchNBT() {
        return matchNBT;
    }

    public ItemStack getEatenItem() {
        return eatenItem;
    }
}
