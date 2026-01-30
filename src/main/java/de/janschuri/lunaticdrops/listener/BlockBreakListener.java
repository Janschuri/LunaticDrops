package de.janschuri.lunaticdrops.listener;

import com.jeff_media.customblockdata.CustomBlockData;
import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropBlockBreak;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.loot.LootFlag;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;
import java.util.stream.Collectors;

public class BlockBreakListener implements Listener {

    private static final Map<BlockDropItemEvent, List<Item>> dropEvents = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockDropLowest(BlockDropItemEvent event) {
        Block block = event.getBlock();
        List<DropFlag> flags = new ArrayList<>();

        PersistentDataContainer blockDataContainer = new CustomBlockData(block, LunaticDrops.getInstance());
        if (blockDataContainer.has(LunaticDrops.PLACED_BY_PLAYER_KEY, org.bukkit.persistence.PersistentDataType.INTEGER)) {
            blockDataContainer.remove(LunaticDrops.PLACED_BY_PLAYER_KEY);
            flags.add(DropFlag.PLAYER_PLACED);
        }

        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        Location location = event.getBlock().getLocation();

        DropBlockBreak blockBreak = (DropBlockBreak) LunaticDrops.getDrop(TriggerType.BLOCK_BREAK, event.getBlockState().getType().name());

        if (blockBreak == null) {
            return;
        }

        if (!blockBreak.isActive()) {
            return;
        }

        int bonusRolls = 0;

        if (isSilk(event.getPlayer().getInventory().getItemInMainHand())) {
            flags.add(DropFlag.SILK_TOUCH);
        }

        if (getFortuneLevel(event.getPlayer().getInventory().getItemInMainHand()) > 0) {
            flags.add(DropFlag.FORTUNE);
            bonusRolls = getFortuneLevel(event.getPlayer().getInventory().getItemInMainHand());
        }

        if (event.getBlockState().getBlockData() instanceof Ageable ageable) {
            if (ageable.getAge() == ageable.getMaximumAge()) {
                flags.add(DropFlag.IS_FULLY_GROWN);
            }
        }

        List<Item> drops = new ArrayList<>();
        boolean eraseVanillaDrops = false;

        for (Loot loot : blockBreak.getLoot()) {
            int rolls = 1;
            boolean debugDrop = event.getPlayer().hasPermission("lunaticdrops.admin.debugdrops.block_break") && LunaticDrops.isDebug();
            List<ItemStack> items = loot.getDrops(bonusRolls, flags);

            if (items.isEmpty()) {
                if (debugDrop) {
                    boolean dropped = false;
                    while (!dropped && rolls < 100000) {
                        rolls++;
                        items = loot.getDrops(bonusRolls, flags);
                        if (!items.isEmpty()) {
                            dropped = true;
                        }
                    }
                } else {
                    continue;
                }
            }

            if (loot.isEraseVanillaDrops()) {
                eraseVanillaDrops = true;
            }

            items.forEach(item -> {
                Item drop = location.getWorld().dropItem(location.clone().add(0.5, 0.5, 0.5), item);
                drops.add(drop);
            });

            loot.runCommands();

            if (debugDrop) {
                Component msg = Component.text("Needed " + rolls + " rolls to get a drop from loot (" + loot.getDisplayItem().getType() + ") with a chance of " + Utils.formatChance(loot.getChance())).color(TextColor.color(0x55FF55));
                event.getPlayer().sendMessage(msg);
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
