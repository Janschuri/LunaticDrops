package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticlib.common.utils.Utils;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.PaginatedList;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EditorGUI extends ListGUI<ItemStack> implements PaginatedList<ItemStack> {

    private static final Map<Integer, Boolean> editModeMap = new HashMap<>();
    private static final Map<Integer, Float> chances = new HashMap<>();
    private static final Map<Integer, Boolean> active = new HashMap<>();
    private static final Map<Integer, List<Loot>> lootMap = new HashMap<>();
    private static final Map<Integer, Integer> pages = new HashMap<>();

    public EditorGUI() {
        super();
        editModeMap.put(getId(), true);
        chances.putIfAbsent(getId(), 0.5f);
        active.putIfAbsent(getId(), true);
    }

    public EditorGUI(CustomDrop customDrop) {
        super();
        editModeMap.put(getId(), false);
        chances.put(getId(), customDrop.getChance());
        active.put(getId(), customDrop.isActive());
    }

    @Override
    public int getPage() {
        return pages.getOrDefault(getId(), 0);
    }

    @Override
    public void setPage(int page) {
        pages.put(getId(), page);
    }

    @Override
    public int getPageSize() {
        return 18;
    }

    @Override
    public int getStartIndex() {
        return 27;
    }

    protected Float getChance() {
        return chances.get(getId());
    }

    protected boolean isActive() {
        return active.get(getId());
    }

    protected boolean isEditMode() {
        return editModeMap.get(getId());
    }

    protected List<Loot> getLoot() {
        lootMap.putIfAbsent(getId(), List.of());
        return lootMap.get(getId());
    }

    protected void setLoot(List<Loot> loot) {
        lootMap.put(getId(), loot);
    }

    protected void addLoot(Loot loot) {
        lootMap.putIfAbsent(getId(), List.of());
        List<Loot> lootList = lootMap.get(getId());
        lootList.add(loot);
        lootMap.put(getId(), lootList);
    }

    @Override
    public void init(Player player) {
        if (!isEditMode()) {
            addButton(17, editButton());
        }
        else if (allowSave()) {
            addButton(17, saveButton());
        }
        else {
            addButton(17, unableToSaveButton());
        }

        addButton(15, increaseChanceButton());
        addButton(14, chanceButton());
        addButton(13, decreaseChanceButton());
        addButton(9, toggleActiveButton());

        if (isEditMode()) {
            addButton(26, addLootButton());
        }

        super.init(player);
    }

    protected abstract boolean allowSave();

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
                    editModeMap.put(getId(), true);
                    reloadGui();
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
                    if (!isEditMode()) {
                        return;
                    }

                    active.put(getId(), !isActive());
                    reloadGui();
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

                    if (!isEditMode()) {
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


                    if (!isEditMode()) {
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

        reloadGui();
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

        reloadGui();
    }


    private InventoryButton unableToSaveButton() {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.RED_STAINED_GLASS_PANE));
    }

    protected abstract void save();

    private InventoryButton addLootButton() {
        ItemStack itemStack = ItemStackUtils.getSkullFromURL("http://textures.minecraft.net/texture/5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1");

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§aAdd new drop");
        itemStack.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> itemStack)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    InventoryGUI gui = new LootGUI(isEditMode())
                            .consumer(loot -> {
                                if (loot == null) {
                                    GUIManager.openGUI(this, player);
                                }

                                addLoot(loot);
                                reloadGui();
                            });

                    GUIManager.openGUI(gui, player);
                });
    }
}
