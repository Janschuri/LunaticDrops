package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Panda;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PandaEatListener implements Listener {

    private final HashMap<UUID, BukkitRunnable> eatingTasks = new HashMap<>();
    private boolean processingEvent = false;

    @EventHandler
    public void onPandaInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.PANDA && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BAMBOO){
            if (setProcessingEvent()) {
                return;
            }

            Logger.debugLog("Player interacted with panda");

            if (((Panda) event.getRightClicked()).isEating()) {
                Logger.debugLog("Panda is already eating! 1");
                return;
            }

            Panda panda = (Panda) event.getRightClicked();
            UUID pandaUUID = panda.getUniqueId();

            if (eatingTasks.containsKey(pandaUUID)) {
                Logger.debugLog("Panda is already eating! 2");
                return;
            }

            if (panda.getEquipment().getItemInMainHand().getType() == Material.BAMBOO) {
                Logger.debugLog("Panda is already eating! 3");
                return;
            }

            boolean isLoveMode = panda.isLoveMode();


            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    Logger.debugLog("Panda is still eating");

                    if (panda.getEquipment().getItemInMainHand().getType() != Material.BAMBOO) {
                        Logger.debugLog("Panda stopped eating");
                        spawnBambooItem(panda.getLocation());
                        eatingTasks.remove(pandaUUID);
                        this.cancel();
                    }
                }
            };

            BukkitRunnable checkTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (isLoveMode != panda.isLoveMode()) {
                        Logger.debugLog("Panda is love mode");
                    } else {
                        task.runTaskTimer(LunaticDrops.getInstance(), 0L, 20L); // Check every second
                        eatingTasks.put(pandaUUID, task);
                    }
                }
            };

            checkTask.runTaskLater(LunaticDrops.getInstance(), 5L);
        }
    }

    private void spawnBambooItem(Location location) {
        ItemStack bambooItem = new ItemStack(Material.DIAMOND, 1);
        ItemMeta bambooMeta = bambooItem.getItemMeta();
        Enchantment protection = Enchantment.PROTECTION_ENVIRONMENTAL;
        ItemFlag hideEnchants = ItemFlag.HIDE_ENCHANTS;
        bambooMeta.addItemFlags(hideEnchants);
//        bambooItem.addEnchantment(protection, 1);
        bambooItem.setItemMeta(bambooMeta);

        Item bamboo = location.getWorld().dropItemNaturally(location , bambooItem);
    }

    private boolean setProcessingEvent() {
        boolean isProcessingEvent = this.processingEvent;
        this.processingEvent = true;
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                processingEvent = false;
            }
        };
        task.runTaskLater(LunaticDrops.getInstance(), 5L);
        return isProcessingEvent;
    }
}
