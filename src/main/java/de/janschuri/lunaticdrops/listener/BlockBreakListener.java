package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockBreakListener implements Listener {

    private static final Map<BlockDropItemEvent, List<Item>> dropEvents = new HashMap<>();

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().forEach(block ->
                Logger.infoLog("Block destroyed by explosion: " + block.getType()));
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.SAND || block.getType() == Material.GRAVEL) {
            Logger.infoLog("Block affected by physics: " + block.getType());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDropLowest(BlockDropItemEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        Location location = event.getBlock().getLocation();

        List<CustomDrop> customDrops = LunaticDrops.getDrops(TriggerType.BLOCK_BREAK);

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
                Logger.debugLog("Block broken and got lucky with " + blockBreak.getDrop().getType());
                Location adjustedLocation = location.clone().add(0.5, 0.5, 0.5);
                Item item = adjustedLocation.getWorld().dropItem(adjustedLocation, blockBreak.getDrop());
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

    public boolean isSilk(ItemStack item) {
        return item.getEnchantments().containsKey(Enchantment.SILK_TOUCH);
    }
}
