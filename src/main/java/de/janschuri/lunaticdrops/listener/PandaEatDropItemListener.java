package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.events.PandaEatDropItemEvent;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.Utils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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

        List<CustomDrop> customDrops = LunaticDrops.getDrops(TriggerType.PANDA_EAT);

        Logger.debugLog("Drops: " + customDrops.size());

        List<ItemStack> drops = new ArrayList<>();

        for (CustomDrop customDrop : customDrops) {
            PandaEat pandaDrop = (PandaEat) customDrop;

            Logger.debugLog("Checking drop: " + pandaDrop.getName());

            if (!pandaDrop.isActive()) {
                Logger.debugLog("Drop is not active");
                continue;
            }

            if (pandaDrop.matchEatenItem(consumedItem)) {
                if (Utils.isLucky(pandaDrop.getChance())) {
                    Logger.debugLog("Panda ate " + consumedItem.getType() + " and got lucky with " + pandaDrop.getName());
                    List<Loot> lootList = pandaDrop.getLoot();

                    for (Loot loot : lootList) {
                        drops.addAll(loot.getDrops());
                    }
                }
            }
        }

        event.getDrops().addAll(drops);
    }
}
