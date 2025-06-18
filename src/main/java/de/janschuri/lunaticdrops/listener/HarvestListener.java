package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropHarvest;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.loot.LootFlag;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Utils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HarvestListener implements Listener {

    @EventHandler
    public void onBlockBreak(PlayerHarvestBlockEvent event) {
        Logger.debugLog("PlayerHarvestBlock: " + event.getHarvestedBlock().getType().name());

        Location location = event.getHarvestedBlock().getLocation();

        DropHarvest harvest = (DropHarvest) LunaticDrops.getDrop(TriggerType.HARVEST, event.getHarvestedBlock().getType().name());

        if (harvest == null) {
            harvest = (DropHarvest) LunaticDrops.getDrop(TriggerType.HARVEST, event.getHarvestedBlock().getType().name()+"_PLANT");
        }

        if (harvest == null) {
            Logger.debugLog("No harvest found for block: " + event.getHarvestedBlock().getType().name());
            return;
        }

        if (!harvest.isActive()) {
            return;
        }

        List<LootFlag> flags = new ArrayList<>();
        int bonusRolls = 0;

        List<ItemStack> drops = new ArrayList<>();
        boolean eraseVanillaDrops = false;

        for (Loot loot : harvest.getLoot()) {
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
                event.getItemsHarvested().clear();
            }

            event.getItemsHarvested().addAll(drops);
        }
    }
}
