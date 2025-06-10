package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.utils.TriggerType;
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

        addButton(53, listLootButton());
        super.init(player);
    }

    @Override
    public String getDefaultTitle() {
        return "All Drops";
    }

    private InventoryButton dropButton(TriggerType dropType) {

        ItemStack itemStack = dropType.getDisplayItem();

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(dropType.getDisplayName());
        int amount = LunaticDrops.getDrops(dropType).size();
        itemMeta.setLore(List.of("Drops: §e" + amount));
        itemStack.setItemMeta(itemMeta);

        return new InventoryButton()
                .creator((player) -> itemStack)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    GUIManager.openGUI(new ListDropGUI(dropType), player);
                });
    }

    private InventoryButton listLootButton() {
        ItemStack itemStack = new ItemStack(Material.CHEST);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§aList Loot");
        itemStack.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> itemStack)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    GUIManager.openGUI(new ListLootGUI(), player);
                });
    }
}