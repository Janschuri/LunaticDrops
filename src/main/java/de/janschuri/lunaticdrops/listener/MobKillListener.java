package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropMobKill;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.loot.LootFlag;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Utils;
import org.bukkit.enchantments.Enchantment;
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

        List<LootFlag> flags = new ArrayList<>();
        int bonusRolls = 0;

        if (event.getEntity().getKiller() == null) {
            flags.add(LootFlag.DROP_ONLY_TO_PLAYER);
        } else {
            if (getLootingLevel(event.getEntity().getKiller().getInventory().getItemInMainHand()) > 0) {
                flags.add(LootFlag.APPLY_LOOTING);
                bonusRolls = getLootingLevel(event.getEntity().getKiller().getInventory().getItemInMainHand());
            }
        }



        List<ItemStack> drops = new ArrayList<>();

        boolean eraseVanillaDrops = false;

        for (Loot loot : mobKill.getLoot()) {

            if (Utils.isLucky(loot.getChance())) {
                loot.runCommands();
                List<ItemStack> items = loot.getDrops(bonusRolls, flags);

                if (items == null) {
                    continue;
                }

                if (items.isEmpty()) {
                    continue;
                }

                if (loot.isEraseVanillaDrops()) {
                    eraseVanillaDrops = true;
                }

                drops.addAll(items);
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
