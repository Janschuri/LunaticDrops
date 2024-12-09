package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import org.bukkit.entity.Player;

import java.util.List;

public class ListDropGUI extends ListGUI<CustomDrop> {

    private DropType dropType;

    public ListDropGUI(DropType dropType) {
        super();
        this.dropType = dropType;
    }

    @Override
    public InventoryButton listItemButton(CustomDrop drop) {
        return new InventoryButton()
                .creator((player) -> drop.getDrop())
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    GUIManager.openGUI(dropType.getEditorGUI(drop).inventory(getInventory()), player);
                });
    }

    @Override
    public List<CustomDrop> getItems() {
        return LunaticDrops.getDrops(dropType);
    }
}