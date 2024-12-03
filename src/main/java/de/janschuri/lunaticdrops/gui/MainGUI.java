package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MainGUI extends InventoryGUI {

    private Player player;

    public MainGUI(Player player) {
        super(createInventory());
        this.player = player;

        decorate(player);
    }

    public MainGUI(MainGUI gui) {
        super(gui.getInventory());

        decorate(gui.player);
    }

    private static Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Panda Eat Drop");

        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }

        return inventory;
    }

    @Override
    public void decorate(Player player) {
        for (int i = 0; i < DropType.values().length; i++) {
            DropType dropType = DropType.values()[i];
            InventoryButton button = dropButton(dropType);
            addButton(10 + i, button);
        }

        super.decorate(player);
    }

    private InventoryButton dropButton(DropType dropType) {

        return new InventoryButton()
                .creator((player) -> dropType.getDisplayItem())
                .consumer(event -> {
                    GUIManager.openGUI(new ListGUI(player, dropType, getInventory()), player, false);
                });
    }
}