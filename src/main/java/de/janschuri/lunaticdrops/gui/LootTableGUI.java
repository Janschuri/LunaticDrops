package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LootTableGUI extends ListGUI<Loot> {

    private static final Map<Integer, Consumer<Loot>> consumerMap = new HashMap<>();

    public LootTableGUI consumer(Consumer<Loot> consumer) {
        consumerMap.put(getId(), consumer);
        return this;
    }

    public Consumer<Loot> getConsumer() {
        return consumerMap.get(getId());
    }

    @Override
    public InventoryButton listItemButton(Loot loot) {
        return null;
    }

    @Override
    public List getItems() {
        return List.of();
    }
}
