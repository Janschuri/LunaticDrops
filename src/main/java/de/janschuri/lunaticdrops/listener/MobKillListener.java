package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropMobKill;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobKillListener implements Listener {

    private static final Map<EntityDeathEvent, List<ItemStack>> dropEvents = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMobKillLowest(EntityDeathEvent event) {
        DropMobKill mobKill = (DropMobKill) LunaticDrops.getDrop(TriggerType.MOB_KILL, event.getEntityType().name());

        if (mobKill == null) {
            return;
        }

        if (!mobKill.isActive()) {
            return;
        }

        List<DropFlag> flags = new ArrayList<>();
        int bonusRolls = 0;

        if (event.getEntity().getKiller() == null) {
            flags.add(DropFlag.NO_PLAYER);
        } else {
            if (getLootingLevel(event.getEntity().getKiller().getInventory().getItemInMainHand()) > 0) {
                flags.add(DropFlag.LOOTING);
                bonusRolls = getLootingLevel(event.getEntity().getKiller().getInventory().getItemInMainHand());
            }
        }



        List<ItemStack> drops = new ArrayList<>();

        boolean eraseVanillaDrops = false;
        Player player = event.getEntity().getKiller();

        for (Loot loot : mobKill.getLoot()) {
            int rolls = 1;
            boolean debugDrop = player.hasPermission("lunaticdrops.admin.debugdrops.block_break") && LunaticDrops.isDebug();


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

            drops.addAll(items);
            loot.runCommands();

            if (debugDrop) {
                Component msg = Component.text("Needed " + rolls + " rolls to get a drop from loot (" + loot.getDisplayItem().getType() + ") with a chance of " + Utils.formatChance(loot.getChance())).color(TextColor.color(0xFF5555));
                player.sendMessage(msg);
            }
        }

        if (!drops.isEmpty()) {
            if (eraseVanillaDrops) {
                event.getDrops().clear();
            }

            event.getDrops().addAll(drops);
        }
    }

    public int getLootingLevel(ItemStack item) {
        if (item == null) {
            return 0;
        }

        if (!item.hasItemMeta()) {
            return 0;
        }

        return item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
    }
}
