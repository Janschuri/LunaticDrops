package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.events.PandaEatDropItemEvent;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Location;
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

        List<CustomDrop> customDrops = LunaticDrops.getDrops(DropType.PANDA_EAT);

        Logger.debugLog("Drops: " + customDrops.size());

        for (CustomDrop customDrop : customDrops) {
            PandaEat pandaDrop = (PandaEat) customDrop;

            Logger.debugLog("Checking drop: " + pandaDrop.getDrop().getType());

            if (!pandaDrop.isActive()) {
                Logger.debugLog("Drop is not active");
                continue;
            }

            if (pandaDrop.matchEatenItem(consumedItem)) {
                Logger.debugLog("Panda ate " + consumedItem.getType() + " and has a chance of " + pandaDrop.getChance() + " to get " + pandaDrop.getDrop().getType());
                if (pandaDrop.isLucky()) {
                    Logger.debugLog("Panda ate " + consumedItem.getType() + " and got lucky with " + pandaDrop.getDrop().getType());
                    event.getDrops().add(pandaDrop.getDrop());
                } else {
                    Logger.debugLog("Panda ate " + consumedItem.getType() + " but was unlucky with " + pandaDrop.getDrop().getType());
                }
            }
        }
    }
}
