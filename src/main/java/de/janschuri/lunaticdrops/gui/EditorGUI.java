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
    private static final Map<Integer, String> names = new HashMap<>();
    private static final Map<Integer, Float> chances = new HashMap<>();
    private static final Map<Integer, ItemStack> dropItems = new HashMap<>();

    public EditorGUI(String name) {
        super();
        editModes.put(getId(), true);
        names.put(getId(), name);
        chances.putIfAbsent(getId(), 0.5f);
        active.putIfAbsent(getId(), true);
    }

    public EditorGUI(CustomDrop customDrop) {
        super();
        editModes.put(getId(), false);
        names.put(getId(), customDrop.getName());
        chances.put(getId(), customDrop.getChance());
        active.put(getId(), customDrop.isActive());
        dropItems.put(getId(), customDrop.getDrop());
    }

    protected String getName() {
        return names.get(getId());
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

        for (Map.Entry<InventoryButton, Integer> entry : getButtons().entrySet()) {
            addButton(entry.getValue(), entry.getKey());
        }

        super.init(player);
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

                    names.remove(getId());
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


    private InventoryButton unableToSaveButton() {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.RED_STAINED_GLASS_PANE));
    }

    protected abstract void save();
}
