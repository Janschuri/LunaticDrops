package de.janschuri.lunaticdrops;

import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Panda;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class PandaEatTask implements Runnable {

    private final List<UUID> eatingTasks = new ArrayList<>();



    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.PANDA) {
                    Panda panda = (Panda) entity;
                    UUID pandaUUID = panda.getUniqueId();
                    if (isPandaEating(panda)) {
                        if (!eatingTasks.contains(pandaUUID)) {
                            eatingTasks.add(panda.getUniqueId());
                        }
                    } else {
                        if (eatingTasks.contains(pandaUUID)) {
                            spawnBambooItem(panda.getLocation());
                            eatingTasks.remove(pandaUUID);
                        }
                    }
                }
            }
        }
    }

    private boolean isPandaEating(Panda panda) {
        return panda.isEating() || panda.getEquipment().getItemInMainHand().getType() != Material.AIR;
    }

    private static void spawnBambooItem(Location location) {
        ItemStack bambooItem = new ItemStack(Material.DIAMOND, 1);
        ItemMeta bambooMeta = bambooItem.getItemMeta();
        Enchantment protection = Enchantment.PROTECTION_ENVIRONMENTAL;
        ItemFlag hideEnchants = ItemFlag.HIDE_ENCHANTS;
        bambooMeta.addItemFlags(hideEnchants);
//        bambooItem.addEnchantment(protection, 1);
        bambooItem.setItemMeta(bambooMeta);

        Item bamboo = location.getWorld().dropItemNaturally(location , bambooItem);
    }
}
