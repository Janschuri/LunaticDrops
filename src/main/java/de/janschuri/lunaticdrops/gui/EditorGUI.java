package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.loot.SingleLoot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.Reopenable;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.PaginatedList;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static de.janschuri.lunaticdrops.utils.Utils.formatChance;

public abstract class EditorGUI extends ListGUI<Loot> implements PaginatedList<Loot>, Reopenable {

    private boolean editMode;
    private boolean active = true;
    private List<Loot> loot = new ArrayList<>();
    private int page = 0;

    private Consumer<InventoryClickEvent> returnConsumer = event -> {
        Player player = (Player) event.getWhoClicked();
        GUIManager.openGUI(new ListDropGUI(getTriggerType()), player);
    };

    public EditorGUI() {
        super();
        editMode = true;
    }

    public EditorGUI(CustomDrop customDrop) {
        super();
        editMode = false;
        active = customDrop.isActive();
        loot.addAll(customDrop.getLoot());
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getPageSize() {
        return 18;
    }

    @Override
    public int getStartIndex() {
        return 27;
    }

    @Override
    public InventoryButton listItemButton(Loot loot) {
        ItemStack item = loot.getItem();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("Chance: " + loot.getChance());

        if (loot instanceof SingleLoot) {
            SingleLoot singleLoot = (SingleLoot) loot;
            lore.add("active: " + (singleLoot.isActive() ? "§aYes" : "§cNo"));
            lore.add("min. Amount: " + singleLoot.getMinAmount());
            lore.add("max. Amount: " + singleLoot.getMaxAmount());
            lore.add("Chance: " + formatChance(singleLoot.getChance()));
            lore.add("");
            if (!singleLoot.getFlags().isEmpty()) {
                lore.add("Flags:");
                singleLoot.getFlags().forEach(flag -> lore.add(" - " + flag.getDisplayName()));
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (loot instanceof SingleLoot) {

                        LootGUI gui = new LootGUI(getTriggerType(), (SingleLoot) loot)
                                .editMode(editMode)
                                .consumer(getLootReturnConsumer(event, loot));

                        GUIManager.openGUI(gui, (Player) event.getWhoClicked());
                    }
                });
    }

    @Override
    public List<Loot> getItems() {
        return loot;
    }

    public EditorGUI consumer(Consumer<InventoryClickEvent> returnConsumer) {
        this.returnConsumer = returnConsumer;
        return this;
    }

    protected boolean isActive() {
        return active;
    }

    protected boolean isEditMode() {
        return editMode;
    }

    protected void setLootList(List<Loot> singleLootList) {
        this.loot = singleLootList;
    }

    protected void addLoot(SingleLoot singleLoot) {
        loot.add(singleLoot);
    }

    @Override
    public void init(Player player) {
        addButton(0, returnButton());

        if (!isEditMode()) {
            addButton(17, editButton());
        }
        else if (allowSave()) {
            addButton(17, saveButton());
        }
        else {
            addButton(17, unableToSaveButton());
        }

        addButton(9, toggleActiveButton());

        if (isEditMode()) {
            addButton(26, addLootButton());
        }

        super.init(player);
    }

    protected abstract boolean allowSave();

    private InventoryButton saveButton() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aSave");
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    save((Player) event.getWhoClicked());
                });
    }

    private InventoryButton returnButton() {
        ItemStack itemStack = new ItemStack(Material.ARROW);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§cReturn");
        itemStack.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> itemStack)
                .consumer(event -> returnConsumer.accept(event));
    }

    private InventoryButton editButton() {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eEdit");
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    editMode = true;
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

                    active = !active;
                    reloadGui();
                });
    }


    private InventoryButton unableToSaveButton() {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.RED_STAINED_GLASS_PANE));
    }

    protected abstract void save(Player player);

    private InventoryButton addLootButton() {
        ItemStack itemStack = ItemStackUtils.getSkullFromURL("http://textures.minecraft.net/texture/5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1");

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§aAdd new drop");
        itemStack.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> itemStack)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    InventoryGUI gui = new LootGUI(getTriggerType()).editMode(editMode)
                            .consumer(getLootReturnConsumer(event, null));

                    GUIManager.openGUI(gui, player);
                });
    }

    private BiConsumer<SingleLoot, Boolean> getLootReturnConsumer(InventoryClickEvent event, Loot oldLoot) {
        return (newLoot, editMode) -> {
            this.editMode = editMode;
            Logger.debugLog(String.format("Loot: %s", newLoot));

            if (newLoot != null) {
                int index = this.loot.indexOf(oldLoot);

                Logger.debugLog(String.format("Index of loot: %d", index));

                if (index < 0) {
                    this.loot.add(newLoot);
                } else {
                    this.loot.set(index, newLoot);
                }
            } else {
                this.loot.remove(oldLoot);
            }

            Logger.debugLog(String.format("this.loot: %s", this.loot));

            GUIManager.openGUI(this, (Player) event.getWhoClicked());
        };
    }

    public abstract TriggerType getTriggerType();

    @Override
    public NamespacedKey uniqueKey() {
        return MainGUI.UNIQUE_KEY;
    }
}
