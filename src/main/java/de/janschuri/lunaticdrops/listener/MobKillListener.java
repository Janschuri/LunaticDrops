package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobKillListener implements Listener {

    private static final Map<EntityDeathEvent, List<Item>> dropEvents = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMobKillLowest(EntityDeathEvent event) {
        Location location = event.getEntity().getLocation();

        List<CustomDrop> customDrops = LunaticDrops.getDrops(TriggerType.MOB_KILL);

        Logger.debugLog("Drops: " + customDrops.size());

//        List<Item> drops = new ArrayList<>();
//
//        for (CustomDrop customDrop : customDrops) {
//            if (!customDrop.isActive()) {
//                continue;
//            }
//
//            MobKill mobKill = (MobKill) customDrop;
//            if (!mobKill.matches(event.getEntityType())) {
//                continue;
//            }
//
//            if (mobKill.isLucky()) {
//                Logger.debugLog("Mob killed and got lucky with " + mobKill.getName());
//                Item item = location.getWorld().dropItem(location, mobKill
//                event.getDrops().add(item.getItemStack());
//                drops.add(item);
//            }
//        }
//
//        if (!drops.isEmpty()) {
//            dropEvents.put(event, drops);
//        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMobKillMonitor(EntityDeathEvent event) {
        if (dropEvents.containsKey(event)) {
            dropEvents.get(event).forEach(drop -> {
                if (!event.getDrops().contains(drop.getItemStack())) {
                    drop.remove();
                }
            });

            dropEvents.remove(event);
        }
    }
}
