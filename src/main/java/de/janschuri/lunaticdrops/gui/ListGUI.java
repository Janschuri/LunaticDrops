package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ListGUI extends InventoryGUI {

    private Player player;
    private DropType dropType;

    public ListGUI(Player player, DropType dropType, Inventory inventory) {
        super(createInventory(inventory));
        this.player = player;
        this.dropType = dropType;

        decorate(player);
    }

    public ListGUI(Player player, DropType dropType) {
        super();
        this.player = player;
        this.dropType = dropType;

        decorate(player);
    }

    public ListGUI(ListGUI gui) {
        super(gui.getInventory());

        decorate(gui.player);
    }

    protected Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Panda Eat Drop");

        return createInventory(inventory);
    }

    private static Inventory createInventory(Inventory inventory) {
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }

        return inventory;
    }

    @Override
    public void decorate(Player player) {
        List<CustomDrop> drops = LunaticDrops.getDrops(dropType);

        for (int i = 0; i < drops.size(); i++) {
            CustomDrop drop = drops.get(i);
            InventoryButton button = dropButton(drop);
            addButton(i+9, button);
        }

        super.decorate(player);
    }

    private InventoryButton dropButton(CustomDrop drop) {
        return new InventoryButton()
                .creator((player) -> drop.getDrop())
                .consumer(event -> {
                    GUIManager.openGUI(dropType.getEditorGUI(player, drop), player);
                });
    }
}