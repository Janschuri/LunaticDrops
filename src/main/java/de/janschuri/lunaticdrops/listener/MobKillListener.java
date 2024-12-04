package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.events.PandaEatDropItemEvent;
import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobKillListener implements Listener {

    private static final Map<EntityDeathEvent, List<Item>> dropEvents = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMobKillLowest(EntityDeathEvent event) {
        Location location = event.getEntity().getLocation();

        List<CustomDrop> customDrops = LunaticDrops.getDrops(DropType.MOB_KILL);

        Logger.debugLog("Drops: " + customDrops.size());

        List<Item> drops = new ArrayList<>();

        for (CustomDrop customDrop : customDrops) {
            if (!customDrop.isActive()) {
                continue;
            }

            MobKill mobKill = (MobKill) customDrop;
            if (!mobKill.matches(event.getEntityType())) {
                continue;
            }

            if (mobKill.isLucky()) {
                Logger.debugLog("Mob killed and got lucky with " + mobKill.getDrop().getType());
                Item item = location.getWorld().dropItem(location, mobKill.getDrop());
                event.getDrops().add(item.getItemStack());
                drops.add(item);
            }
        }

        if (!drops.isEmpty()) {
            dropEvents.put(event, drops);
        }
    }
}
