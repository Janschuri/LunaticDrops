package de.janschuri.lunaticdrops.events;

import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Panda;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PandaEatTask implements Runnable {

    private final Map<UUID, ItemStack> eatingTasks = new HashMap<>();



    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.PANDA) {
                    Panda panda = (Panda) entity;
                    UUID pandaUUID = panda.getUniqueId();
                    ItemStack item = panda.getEquipment().getItemInMainHand();
                    if (isPandaEating(panda)) {
                        if (!eatingTasks.containsKey(pandaUUID)) {
                            Logger.debugLog("Panda started eating");
                            eatingTasks.put(panda.getUniqueId(), item);
                        }
                    } else {
                        if (eatingTasks.containsKey(pandaUUID)) {
                            Logger.debugLog("Panda stopped eating");
                            PandaEatDropItemEvent event = new PandaEatDropItemEvent(panda, eatingTasks.get(pandaUUID));
                            Bukkit.getPluginManager().callEvent(event);
                            eatingTasks.remove(pandaUUID);
                        }
                    }
                }
            }
        }
    }

    private boolean isPandaEating(Panda panda) {
        return panda.getEquipment().getItemInMainHand().getType() != Material.AIR;
    }
}
