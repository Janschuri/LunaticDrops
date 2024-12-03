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

    private final Integer id;
    private static final AtomicInteger atomicInteger = new AtomicInteger();
    private static final Map<Integer, Boolean> editModes = new HashMap<>();
    private static final Map<Integer, Player> players = new HashMap<>();
    private static final Map<Integer, String> names = new HashMap<>();
    private static final Map<Integer, Float> chances = new HashMap<>();
    private static final Map<Integer, Boolean> active = new HashMap<>();
    private static final Map<Integer, ItemStack> dropItems = new HashMap<>();

    public EditorGUI(Player player, String name) {
        super(createInventory());
        this.id = atomicInteger.getAndIncrement();
        editModes.put(id, true);
        players.put(id, player);
        names.put(id, name);
        chances.putIfAbsent(id, 1.0f);
        active.putIfAbsent(id, true);

        decorate(player);
    }

    public EditorGUI(Player player, CustomDrop customDrop) {
        super(createInventory());
        this.id = atomicInteger.getAndIncrement();
        editModes.put(id, false);
        players.put(id, player);
        names.put(id, customDrop.getName());
        chances.put(id, customDrop.getChance());
        active.put(id, customDrop.isActive());
        dropItems.put(id, customDrop.getDrop());


        decorate(player);
    }

    public EditorGUI(Player player, String name, Inventory inventory) {
        super(inventory);
        this.id = atomicInteger.getAndIncrement();
        editModes.put(id, true);
        players.put(id, player);
        names.put(id, name);
        chances.putIfAbsent(id, 1.0f);
        active.putIfAbsent(id, true);

        decorate(player);
    }

    public EditorGUI(Player player, CustomDrop customDrop, Inventory inventory) {
        super(inventory);
        this.id = atomicInteger.getAndIncrement();
        editModes.put(id, false);
        players.put(id, player);
        names.put(id, customDrop.getName());
        chances.put(id, customDrop.getChance());
        active.put(id, customDrop.isActive());
        dropItems.put(id, customDrop.getDrop());


        decorate(player);
    }

    protected Integer getId() {
        return id;
    }

    protected Player getPlayer() {
        return players.get(id);
    }

    protected String getName() {
        return names.get(id);
    }

    protected Float getChance() {
        return chances.get(id);
    }

    protected boolean isActive() {
        return active.get(id);
    }

    protected boolean isEditMode() {
        return editModes.get(id);
    }

    protected ItemStack getDropItem() {
        return dropItems.get(id);
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
        return names.get(id) != null
                && dropItems.get(id) != null
                && chances.get(id) != null;
    }

    private InventoryButton saveButton() {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.LIME_STAINED_GLASS_PANE))
                .consumer(event -> {
                    save();

                    names.remove(id);
                    chances.remove(id);

                    event.getWhoClicked().closeInventory();
                });
    }

    private InventoryButton editButton() {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    editModes.put(id, true);
                    reloadGui();
                });
    }

    private InventoryButton createAddDropItemButton() {

        ItemStack item = dropItems.get(id) == null ? new ItemStack(Material.AIR) : dropItems.get(id);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!isEditMode()) {
                        return;
                    }

                    ItemStack cursorItem = event.getCursor();
                    if (cursorItem == null || cursorItem.getType() == Material.AIR) {
                        return;
                    }

                    ItemStack newItem = cursorItem.clone();
                    newItem.setAmount(1);

                    dropItems.put(id, newItem);

                    reloadGui();
                });
    }

    private InventoryButton chanceButton() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + chances.get(id));
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

    protected void reloadGui() {
        Player player = players.get(id);
        GUIManager.openGUI(this, player);
        this.decorate(player);
    }

    protected abstract void save();
}
