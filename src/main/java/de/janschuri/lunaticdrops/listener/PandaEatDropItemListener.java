package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.events.PandaEatDropItemEvent;
import de.janschuri.lunaticdrops.drops.Drop;
import de.janschuri.lunaticdrops.drops.DropPandaEat;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class PandaEatDropItemListener implements Listener {

    public static List<Item> droppedItems = new ArrayList<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPandaPickupItem(EntityPickupItemEvent event) {
        if (event.getEntityType() == EntityType.PANDA && droppedItems.contains(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPandaEatDropItem(PandaEatDropItemEvent event) {
        Location location = event.getPanda().getLocation();

        if (event.getDrops().isEmpty()) {
            return;
        }

        //play sound on location
        location.getWorld().playSound(location, Sound.ENTITY_CHICKEN_EGG, 1, 1);

        for (ItemStack drop : event.getDrops()) {
            Item item = location.getWorld().dropItem(location, drop);

            droppedItems.add(item);

            Bukkit.getScheduler().runTaskLater(LunaticDrops.getInstance(), () -> {
                droppedItems.remove(item);
                item.remove();
            }, 20 * 60);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPandaEatDropItemLowest(PandaEatDropItemEvent event) {

        ItemStack consumedItem = event.getConsumedItem();

        List<Drop> customDrops = LunaticDrops.getDrops(TriggerType.PANDA_EAT);

        Logger.debug("Drops: " + customDrops.size());

        List<ItemStack> drops = new ArrayList<>();

        for (Drop customDrop : customDrops) {
            DropPandaEat pandaDrop = (DropPandaEat) customDrop;

            Logger.debug("Checking drop: " + pandaDrop.getName());

            if (!pandaDrop.isActive()) {
                Logger.debug("Drop is not active");
                continue;
            }

            if (pandaDrop.matchEatenItem(consumedItem)) {
                    List<Loot> lootList = pandaDrop.getLoot();

                    for (Loot loot : lootList) {
                        List<ItemStack> itemDrops = loot.getDrops();

                        if (!itemDrops.isEmpty()) {
                            loot.runCommands();
                        }

                        drops.addAll(itemDrops);
                    }
            }
        }

        event.getDrops().addAll(drops);
    }
}
