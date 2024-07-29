package de.janschuri.lunaticdrops;

import de.janschuri.lunaticdrops.drops.CustomDropPandaEat;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PandaEatDropItemListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPandaEatDropItem(PandaEatDropItemEvent event) {
        Location location = event.getPanda().getLocation();

        for (ItemStack drop : event.getDrops()) {
            location.getWorld().dropItem(location, drop);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPandaEatDropItemLowest(PandaEatDropItemEvent event) {

        ItemStack consumedItem = event.getConsumedItem();

        List<CustomDropPandaEat> customDrops = LunaticDrops.getPandaEatDrops();

        Logger.debugLog("Drops: " + customDrops.size());

        for (CustomDropPandaEat customDrop : customDrops) {
            Logger.debugLog("Checking drop: " + customDrop.getDrop().getType());

            if (!customDrop.isActive()) {
                Logger.debugLog("Drop is not active");
                continue;
            }

            if (customDrop.matchEatenItem(consumedItem)) {
                Logger.debugLog("Panda ate " + consumedItem.getType() + " and has a chance of " + customDrop.getChance() + " to get " + customDrop.getDrop().getType());
                if (customDrop.isLucky()) {
                    Logger.debugLog("Panda ate " + consumedItem.getType() + " and got lucky with " + customDrop.getDrop().getType());
                    event.getDrops().add(customDrop.getDrop());
                } else {
                    Logger.debugLog("Panda ate " + consumedItem.getType() + " but was unlucky with " + customDrop.getDrop().getType());
                }
            }
        }
    }
}
