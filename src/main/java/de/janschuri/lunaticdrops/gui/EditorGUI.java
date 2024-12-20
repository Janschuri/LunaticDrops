package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.nio.file.LinkOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class EditorGUI extends InventoryGUI {

    private static final Map<Integer, Boolean> editModes = new HashMap<>();
    private static final Map<Integer, Float> chances = new HashMap<>();
    private static final Map<Integer, Boolean> active = new HashMap<>();
    private static final Map<Integer, ItemStack> dropItems = new HashMap<>();

    public EditorGUI() {
        super();
        editModes.put(getId(), true);
        chances.putIfAbsent(getId(), 0.5f);
        active.putIfAbsent(getId(), true);
    }

    public EditorGUI(CustomDrop customDrop) {
        super();
        editModes.put(getId(), false);
        chances.put(getId(), customDrop.getChance());
        active.put(getId(), customDrop.isActive());
        dropItems.put(getId(), customDrop.getDrop());
    }

    protected Float getChance() {
        return chances.get(getId());
    }

    protected boolean isActive() {
        return active.get(getId());
    }

    protected boolean isEditMode() {
        return editModes.get(getId());
    }

    protected ItemStack getDropItem() {
        return dropItems.get(getId());
    }

    @Override
    public void init(Player player) {
        if (!isEditMode()) {
            addButton(35, editButton());
        }
        else if (allowSave()) {
            addButton(35, saveButton());
        } else {
            addButton(35, unableToSaveButton());
        }

        addButton(22, createAddDropItemButton());
        addButton(15, increaseChanceButton());
        addButton(24, chanceButton());
        addButton(33, decreaseChanceButton());
        addButton(8, toggleActiveButton());

        for (Map.Entry<InventoryButton, Integer> entry : getButtons().entrySet()) {
            addButton(entry.getValue(), entry.getKey());
        }

        super.init(player);
    }

    protected abstract Map<InventoryButton, Integer> getButtons();

    protected boolean allowSave() {
        return getDropItem() != null
                && getChance() != null;
    }

    private InventoryButton saveButton() {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.LIME_STAINED_GLASS_PANE))
                .consumer(event -> {
                    save();

                    chances.remove(getId());

                    event.getWhoClicked().closeInventory();
                });
    }

    private InventoryButton editButton() {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    editModes.put(getId(), true);
                    reloadGui(player);
                });
    }

    private InventoryButton toggleActiveButton() {
        ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Active: " + (isActive() ? "§aYes" : "§cNo"));
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    active.put(getId(), !isActive());
                    reloadGui((Player) event.getWhoClicked());
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

                    dropItems.put(getId(), newItem);

                    reloadGui(player);
                });
    }

    private InventoryButton chanceButton() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + formatChance(getChance()));
        List<String> lore = List.of("Chance for the drop to happen");
        meta.setLore(lore);
        item.setItemMeta(meta);


        return new InventoryButton()
                .creator((player) -> item);
    }

    private InventoryButton increaseChanceButton() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + formatChance(getChance()));
        List<String> lore = List.of("Chance for the drop to happen");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (processingClickEvent()) {
                        return;
                    }

                    ClickType clickType = event.getClick();

                    if (clickType == ClickType.WINDOW_BORDER_LEFT || clickType == ClickType.WINDOW_BORDER_RIGHT) {
                        return;
                    }

                    switch (clickType) {
                        case SHIFT_RIGHT:
                            increaseChance((Player) event.getWhoClicked(), 0.0001f);
                            break;
                        case RIGHT:
                            increaseChance((Player) event.getWhoClicked(), 0.001f);
                            break;
                        case LEFT:
                            increaseChance((Player) event.getWhoClicked(), 0.01f);
                            break;
                        case SHIFT_LEFT:
                            increaseChance((Player) event.getWhoClicked(), 0.1f);
                            break;
                    }
                });
    }

    private InventoryButton decreaseChanceButton() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + formatChance(getChance()));
        List<String> lore = List.of("Chance for the drop to happen");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (processingClickEvent()) {
                        return;
                    }

                    ClickType clickType = event.getClick();

                    if (clickType == ClickType.WINDOW_BORDER_LEFT || clickType == ClickType.WINDOW_BORDER_RIGHT) {
                        return;
                    }

                    switch (clickType) {
                        case SHIFT_RIGHT:
                            decreaseChance((Player) event.getWhoClicked(), 0.0001f);
                            break;
                        case RIGHT:
                            decreaseChance((Player) event.getWhoClicked(), 0.001f);
                            break;
                        case LEFT:
                            decreaseChance((Player) event.getWhoClicked(), 0.01f);
                            break;
                        case SHIFT_LEFT:
                            decreaseChance((Player) event.getWhoClicked(), 0.1f);
                            break;
                    }
                });
    }

    private void decreaseChance(Player player, float amount) {
        float newChance = getChance() - amount;

        if (newChance < 0) {
            newChance = 0;
        }

        chances.put(getId(), newChance);

        reloadGui(player);
    }

    private static String formatChance(float chance) {
        return String.format("%.2f", chance * 100) + "%";
    }

    private void increaseChance(Player player, float amount) {
        float newChance = getChance() + amount;

        if (newChance > 1) {
            newChance = 1;
        }

        chances.put(getId(), newChance);

        reloadGui(player);
    }


    private InventoryButton unableToSaveButton() {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.RED_STAINED_GLASS_PANE));
    }

    protected abstract void save();
}
