package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MainGUI extends InventoryGUI {

    public MainGUI() {
        super();
    }

    protected Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Panda Eat Drop");

        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }

        return inventory;
    }

    @Override
    public void init(Player player) {
        for (int i = 0; i < TriggerType.values().length; i++) {
            TriggerType dropType = TriggerType.values()[i];
            InventoryButton button = dropButton(dropType);
            addButton(10 + i, button);
        }

        super.init(player);
    }

    private InventoryButton dropButton(TriggerType dropType) {

        return new InventoryButton()
                .creator((player) -> dropType.getDisplayItem())
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    GUIManager.openGUI(new ListDropGUI(dropType), player);
                });
    }
}