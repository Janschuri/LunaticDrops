package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface Loot {

    List<ItemStack> getDrops();
    float getChance();
    Map<String, Object> toMap();
    ItemStack getItem();
    boolean isEraseVanillaDrops();
}
