package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PandaEatDropGUI extends InventoryGUI {

    private final String name;
    private static final Map<String, Player> players = new HashMap<>();
    private static final Map<String, ItemStack> eatItems = new HashMap<>();
    private static final Map<String, ItemStack> dropItems = new HashMap<>();
    private static final Map<String, Float> chances = new HashMap<>();
    private static final Map<String, Boolean> matchNBT = new HashMap<>();
    private static final Map<String, Boolean> active = new HashMap<>();

    public PandaEatDropGUI(Player player, String name) {
        super(createInventory());
        this.name = name;
        players.put(name, player);
        Logger.debugLog("EatItemsInit: " + eatItems);
        chances.putIfAbsent(name, 1.0f);
        matchNBT.putIfAbsent(name, false);
        active.putIfAbsent(name, true);

        decorate(player);
    }

    public PandaEatDropGUI(PandaEatDropGUI gui) {
        super(gui.getInventory());
        this.name = gui.name;
        Player player = players.get(name);

        decorate(player);
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
        addButton(29, createAddEatItemButton());
        addButton(33, createAddDropItemButton());

        if (allowSave()) {
            addButton(35, saveButton());
        } else {
            addButton(35, unableToSaveButton());
        }


        super.decorate(player);
    }


    private boolean allowSave() {
        return eatItems.get(name) != null
                && dropItems.get(name) != null
                && chances.get(name) != null
                && active.get(name) != null
                && matchNBT.get(name) != null;
    }

    private InventoryButton createAddEatItemButton() {

        ItemStack item = eatItems.get(name) == null ? new ItemStack(Material.AIR) : eatItems.get(name);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    ItemStack cursorItem = event.getCursor();
                    if (cursorItem == null || cursorItem.getType() == Material.AIR) {
                        return;
                    }

                    ItemStack newItem = cursorItem.clone();
                    newItem.setAmount(1);

                    eatItems.put(name, newItem);

                    reloadGui();
                });
    }

    private InventoryButton createAddDropItemButton() {

        ItemStack item = dropItems.get(name) == null ? new ItemStack(Material.AIR) : dropItems.get(name);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    ItemStack cursorItem = event.getCursor();
                    if (cursorItem == null || cursorItem.getType() == Material.AIR) {
                        return;
                    }

                    ItemStack newItem = cursorItem.clone();
                    newItem.setAmount(1);

                    dropItems.put(name, newItem);

                    reloadGui();
                });
    }

    private InventoryButton saveButton() {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.LIME_STAINED_GLASS_PANE))
                .consumer(event -> {
                    save();
                    event.getWhoClicked().closeInventory();
                });
    }

    private InventoryButton chanceButton() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + chances.get(name));
        List<String> lore = List.of("Chance for the drop to happen");
        meta.setLore(lore);
        item.setItemMeta(meta);


        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    save();
                    event.getWhoClicked().closeInventory();
                });
    }

    private InventoryButton unableToSaveButton() {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.RED_STAINED_GLASS_PANE));
    }

    private void reloadGui() {
        Player player = players.get(name);
        GUIManager.openGUI(new PandaEatDropGUI(this), player, false);
    }

    private void save() {
        PandaEat pandaEat = new PandaEat(
                name,
                dropItems.get(name),
                chances.get(name),
                active.get(name),
                eatItems.get(name),
                matchNBT.get(name)
        );

        pandaEat.save();
    }
}
