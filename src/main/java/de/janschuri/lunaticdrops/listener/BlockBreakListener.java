package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.loot.LootFlag;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.Utils;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDropLowest(BlockDropItemEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        Logger.debugLog("BlockDrop: " + event.getBlock().getType().name());

        Location location = event.getBlock().getLocation();

        BlockBreak blockBreak = (BlockBreak) LunaticDrops.getDrop(TriggerType.BLOCK_BREAK, event.getBlockState().getType().name());

        if (blockBreak == null) {
            return;
        }

        if (!blockBreak.isActive()) {
            return;
        }

        List<LootFlag> flags = new ArrayList<>();
        int bonusRolls = 0;

        if (isSilk(event.getPlayer().getInventory().getItemInMainHand())) {
            flags.add(LootFlag.DROP_WITH_SILK_TOUCH);
        }

        if (getFortuneLevel(event.getPlayer().getInventory().getItemInMainHand()) > 0) {
            flags.add(LootFlag.APPLY_FORTUNE);
            bonusRolls = getFortuneLevel(event.getPlayer().getInventory().getItemInMainHand());
        }

        List<Item> drops = new ArrayList<>();
        boolean eraseVanillaDrops = false;

        for (Loot loot : blockBreak.getLoot()) {
            if (Utils.isLucky(loot.getChance())) {
                List<ItemStack> items = loot.getDrops(flags, bonusRolls);

                if (items == null) {
                    continue;
                }

                if (items.isEmpty()) {
                    continue;
                }

                if (loot.isEraseVanillaDrops()) {
                    eraseVanillaDrops = true;
                }

                items.forEach(item -> {
                    Item drop = location.getWorld().dropItem(location.clone().add(0.5, 0.5, 0.5), item);
                    drops.add(drop);
                });
            }
        }


        if (!drops.isEmpty()) {
            if (eraseVanillaDrops) {
                event.getItems().clear();
            }

            event.getItems().addAll(drops);
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
        if (item == null) {
            return false;
        }

        if (!item.hasItemMeta()) {
            return false;
        }

        return item.getEnchantments().containsKey(Enchantment.SILK_TOUCH);
    }

    public int getFortuneLevel(ItemStack item) {
        if (item == null) {
            return 0;
        }

        if (!item.hasItemMeta()) {
            return 0;
        }

        return item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
    }
}
