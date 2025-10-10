package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropEntityBreed;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EntityBreedListener implements Listener {


    @EventHandler
    public void onMobBreed(EntityBreedEvent event) {
        DropEntityBreed entityBreed = (DropEntityBreed) LunaticDrops.getDrop(TriggerType.ENTITY_BREED, event.getEntityType().name());

        if (entityBreed == null) {
            return;
        }

        if (!entityBreed.isActive()) {
            return;
        }

        List<DropFlag> flags = new ArrayList<>();
        int bonusRolls = 0;

        if (!(event.getBreeder() instanceof Player)) {
            flags.add(DropFlag.NO_PLAYER);
        }

        List<ItemStack> drops = new ArrayList<>();

        for (Loot loot : entityBreed.getLoot()) {

            if (Utils.isLucky(loot.getChance())) {
                loot.runCommands();
                List<ItemStack> items = loot.getDrops(bonusRolls, flags);

                if (items == null) {
                    continue;
                }

                if (items.isEmpty()) {
                    continue;
                }

                drops.addAll(items);
            }
        }

        if (!drops.isEmpty()) {
            Location location = event.getEntity().getLocation();

            drops.forEach(itemStack -> {
                location.getWorld().dropItem(location.clone().add(0.5, 0.5, 0.5), itemStack);
            });
        }
    }
}
