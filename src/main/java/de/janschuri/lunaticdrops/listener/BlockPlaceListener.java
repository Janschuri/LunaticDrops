package de.janschuri.lunaticdrops.listener;

import com.jeff_media.customblockdata.CustomBlockData;
import de.janschuri.lunaticdrops.LunaticDrops;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;

import static de.janschuri.lunaticdrops.LunaticDrops.PLACED_BY_PLAYER_KEY;

public class BlockPlaceListener  implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent event) {
        org.bukkit.block.Block block = event.getBlock();
        PersistentDataContainer blockDataContainer = new CustomBlockData(block, LunaticDrops.getInstance());
        blockDataContainer.set(PLACED_BY_PLAYER_KEY, org.bukkit.persistence.PersistentDataType.INTEGER, 1);
    }
}
