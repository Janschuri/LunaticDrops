package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.loot.SingleLoot;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LootTableGUI extends ListGUI<SingleLoot> {

    private static final Map<Integer, Consumer<SingleLoot>> consumerMap = new HashMap<>();

    public LootTableGUI consumer(Consumer<SingleLoot> consumer) {
        consumerMap.put(getId(), consumer);
        return this;
    }

    public Consumer<SingleLoot> getConsumer() {
        return consumerMap.get(getId());
    }

    @Override
    public InventoryButton listItemButton(SingleLoot singleLoot) {
        return null;
    }

    @Override
    public List getItems() {
        return List.of();
    }
}
