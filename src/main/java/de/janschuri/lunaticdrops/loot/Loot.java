package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Random;

public interface Loot {

    List<ItemStack> getDrops();
    float getChance();
    Map<String, Object> toMap();
}
