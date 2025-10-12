package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropEntityBreed;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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

        Player player = (Player) event.getBreeder();

        List<ItemStack> drops = new ArrayList<>();

        for (Loot loot : entityBreed.getLoot()) {
            int rolls = 1;
            boolean debugDrop = player.hasPermission("lunaticdrops.admin.debugdrops.entity_breed") && LunaticDrops.isDebug();

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

            drops.addAll(items);

            loot.runCommands();

            if (debugDrop) {
                Component msg = Component.text("Needed " + rolls + " rolls to get a drop from loot (" + loot.getDisplayItem().getType() + ") with a chance of " + Utils.formatChance(loot.getChance())).color(TextColor.color(0xFF5555));
                player.sendMessage(msg);
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
