package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropHarvest;
import de.janschuri.lunaticdrops.drops.DropLeavesDecay;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.loot.LootFlag;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LeavesDecayListener  implements Listener {

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        DropLeavesDecay leaves = (DropLeavesDecay) LunaticDrops.getDrop(TriggerType.LEAVES_DECAY, event.getBlock().getType().name());

        if (leaves == null) {
            return;
        }

        if (!leaves.isActive()) {
            return;
        }

        List<LootFlag> flags = new ArrayList<>();
        int bonusRolls = 0;

        List<ItemStack> drops = new ArrayList<>();
        boolean eraseVanillaDrops = false;

        for (Loot loot : leaves.getLoot()) {
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
            Block block = event.getBlock();

            if (eraseVanillaDrops) {
                event.setCancelled(true);
                block.setType(Material.AIR);
            }

            Location location = block.getLocation();

            drops.forEach(itemStack -> {
                location.getWorld().dropItem(location.clone().add(0.5, 0.5, 0.5), itemStack);
            });
        }
    }
}
