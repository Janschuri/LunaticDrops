package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Item;
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
        MobKill mobKill = (MobKill) LunaticDrops.getDrop(TriggerType.MOB_KILL, event.getEntityType().name());

        if (mobKill == null) {
            return;
        }

        if (!mobKill.isActive()) {
            return;
        }

        List<ItemStack> drops = new ArrayList<>();

        boolean eraseVanillaDrops = false;

        for (Loot loot : mobKill.getLoot()) {

            if (Utils.isLucky(loot.getChance())) {
                List<ItemStack> items = loot.getDrops();

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
}
