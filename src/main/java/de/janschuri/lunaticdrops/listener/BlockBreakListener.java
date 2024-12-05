package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockBreakListener implements Listener {

    private static final Map<BlockDropItemEvent, List<Item>> dropEvents = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDropLowest(BlockDropItemEvent event) {
        Location location = event.getBlock().getLocation();

        List<CustomDrop> customDrops = LunaticDrops.getDrops(DropType.BLOCK_BREAK);

        Logger.debugLog("Drops: " + customDrops.size());

        List<Item> drops = new ArrayList<>();

        for (CustomDrop customDrop : customDrops) {
            Logger.debugLog("Checking drop: " + customDrop.getName());
            if (!customDrop.isActive()) {
                continue;
            }

            BlockBreak blockBreak = (BlockBreak) customDrop;
            if (!blockBreak.matches(event.getBlockState())) {
                continue;
            }

            if (blockBreak.isLucky()) {
                Logger.debugLog("Mob killed and got lucky with " + blockBreak.getDrop().getType());
                Item item = location.getWorld().dropItem(location, blockBreak.getDrop());
                event.getItems().add(item);
                drops.add(item);
            }
        }

        if (!drops.isEmpty()) {
            dropEvents.put(event, drops);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreakMonitor(BlockDropItemEvent event) {
        if (dropEvents.containsKey(event)) {
            dropEvents.get(event).forEach(drop -> {
                if (!event.getItems().contains(drop)) {
                    drop.remove();
                }
            });

            dropEvents.remove(event);
        }
    }
}
