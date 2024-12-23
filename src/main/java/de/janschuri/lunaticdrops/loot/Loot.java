package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Loot {


    default List<ItemStack> getDrops() {
        return getDrops(new ArrayList<>(), 0);
    }
    List<ItemStack> getDrops(List<LootFlag> flags, int bonusRolls);
    float getChance();
    Map<String, Object> toMap();
    ItemStack getItem();
    boolean isEraseVanillaDrops();
}
