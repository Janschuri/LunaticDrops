package de.janschuri.lunaticdrops.listener;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropHarvest;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HarvestListener implements Listener {

    @EventHandler
    public void onBlockBreak(PlayerHarvestBlockEvent event) {
        Logger.debug("PlayerHarvestBlock: " + event.getHarvestedBlock().getType().name());

        Location location = event.getHarvestedBlock().getLocation();

        DropHarvest harvest = (DropHarvest) LunaticDrops.getDrop(TriggerType.HARVEST, event.getHarvestedBlock().getType().name());

        if (harvest == null) {
            harvest = (DropHarvest) LunaticDrops.getDrop(TriggerType.HARVEST, event.getHarvestedBlock().getType().name()+"_PLANT");
        }

        if (harvest == null) {
            Logger.debug("No harvest found for block: " + event.getHarvestedBlock().getType().name());
            return;
        }

        if (!harvest.isActive()) {
            return;
        }

        List<DropFlag> flags = new ArrayList<>();
        int bonusRolls = 0;

        List<ItemStack> drops = new ArrayList<>();
        boolean eraseVanillaDrops = false;

        Player player = event.getPlayer();

        for (Loot loot : harvest.getLoot()) {
            int rolls = 1;
            boolean debugDrop = player.hasPermission("lunaticdrops.admin.debugdrops.harvest") && LunaticDrops.isDebug();

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

            if (loot.isEraseVanillaDrops()) {
                eraseVanillaDrops = true;
            }

            drops.addAll(items);

            loot.runCommands();

            if (debugDrop) {
                Component msg = Component.text("Needed " + rolls + " rolls to get a drop from loot (" + loot.getDisplayItem().getType() + ") with a chance of " + Utils.formatChance(loot.getChance())).color(TextColor.color(0xFF5555));
                player.sendMessage(msg);
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
