package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.drops.CustomDrop;
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
import java.util.concurrent.atomic.AtomicInteger;

public abstract class EditorGUI extends InventoryGUI {

    private final Inventory inventory;
    private static final Map<Inventory, Boolean> editModes = new HashMap<>();
    private static final Map<Inventory, String> names = new HashMap<>();
    private static final Map<Inventory, Float> chances = new HashMap<>();
    private static final Map<Inventory, Boolean> active = new HashMap<>();
    private static final Map<Inventory, ItemStack> dropItems = new HashMap<>();

    public EditorGUI(Player player, String name) {
        super();
        this.inventory = getInventory();
        editModes.put(inventory, true);
        names.put(inventory, name);
        chances.putIfAbsent(inventory, 1.0f);
        active.putIfAbsent(inventory, true);

        decorate(player);
    }

    public EditorGUI(Player player, CustomDrop customDrop) {
        super();
        this.inventory = getInventory();
        editModes.put(inventory, false);
        names.put(inventory, customDrop.getName());
        chances.put(inventory, customDrop.getChance());
        active.put(inventory, customDrop.isActive());
        dropItems.put(inventory, customDrop.getDrop());


        decorate(player);
    }

    public EditorGUI(Player player, String name, Inventory inventory) {
        super(inventory);
        this.inventory = inventory;
        editModes.put(inventory, true);
        names.put(inventory, name);
        chances.putIfAbsent(inventory, 1.0f);
        active.putIfAbsent(inventory, true);

        decorate(player);
    }

    public EditorGUI(Player player, CustomDrop customDrop, Inventory inventory) {
        super(inventory);
        this.inventory = inventory;
        editModes.put(inventory, false);
        names.put(inventory, customDrop.getName());
        chances.put(inventory, customDrop.getChance());
        active.put(inventory, customDrop.isActive());
        dropItems.put(inventory, customDrop.getDrop());


        decorate(player);
    }

    protected String getName() {
        return names.get(inventory);
    }

    protected Float getChance() {
        return chances.get(inventory);
    }

    protected boolean isActive() {
        return active.get(inventory);
    }

    protected boolean isEditMode() {
        return editModes.get(inventory);
    }

    protected ItemStack getDropItem() {
        return dropItems.get(inventory);
    }

    @Override
    protected Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Panda Eat Drop");

        return inventory;
    }

    @Override
    public void decorate(Player player) {
        if (!isEditMode()) {
            addButton(35, editButton());
        }
        else if (allowSave()) {
            addButton(35, saveButton());
        } else {
            addButton(35, unableToSaveButton());
        }

        addButton(24, createAddDropItemButton());

        for (Map.Entry<InventoryButton, Integer> entry : getButtons().entrySet()) {
            addButton(entry.getValue(), entry.getKey());
        }

        super.decorate(player);
    }

    protected abstract Map<InventoryButton, Integer> getButtons();

    protected boolean allowSave() {
        return getName() != null
                && getDropItem() != null
                && getChance() != null;
    }

    private InventoryButton saveButton() {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.LIME_STAINED_GLASS_PANE))
                .consumer(event -> {
                    save();

                    names.remove(inventory);
                    chances.remove(inventory);

                    event.getWhoClicked().closeInventory();
                });
    }

    private InventoryButton editButton() {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    editModes.put(inventory, true);
                    reloadGui(player);
                });
    }

    private InventoryButton createAddDropItemButton() {

        ItemStack item = getDropItem() == null ? new ItemStack(Material.AIR) : getDropItem();

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!isEditMode()) {
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();

                    ItemStack cursorItem = event.getCursor();
                    if (cursorItem == null || cursorItem.getType() == Material.AIR) {
                        return;
                    }

                    ItemStack newItem = cursorItem.clone();
                    newItem.setAmount(1);

                    dropItems.put(inventory, newItem);

                    reloadGui(player);
                });
    }

    private InventoryButton chanceButton() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + getChance());
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

    protected abstract void save();
}
