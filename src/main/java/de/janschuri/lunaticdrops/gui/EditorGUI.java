package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.loot.SingleLoot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.PaginatedList;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class EditorGUI extends ListGUI<Loot> implements PaginatedList<Loot> {

    private boolean editMode = false;
    private float chance = 0.5f;
    private boolean active = true;
    private List<Loot> loot = new ArrayList<>();
    private int page = 0;

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
        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!isEditMode()) {
                        return;
                    }

                    if (loot instanceof SingleLoot) {
                        LootGUI gui = new LootGUI(getTriggerType(), (SingleLoot) loot)
                                .consumer(newLoot -> {
                                    if (newLoot != null) {
                                        int index = this.loot.indexOf(loot);
                                        this.loot.set(index, newLoot);
                                    } else {
                                        this.loot.remove(loot);
                                    }

                                    GUIManager.openGUI(this, (Player) event.getWhoClicked());
                                });

                        GUIManager.openGUI(gui, (Player) event.getWhoClicked());
                    }
                });
    }

    @Override
    public List<Loot> getItems() {
        return loot;
    }

    protected Float getChance() {
        return chance;
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
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.LIME_STAINED_GLASS_PANE))
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
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    GUIManager.openGUI(new ListDropGUI(getTriggerType()), player);
                });
    }

    private InventoryButton editButton() {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);

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

                    InventoryGUI gui = new LootGUI(getTriggerType(), isEditMode())
                            .consumer(loot -> {
                                if (loot != null) {
                                    addLoot(loot);
                                }

                                GUIManager.openGUI(this, player);
                            });

                    GUIManager.openGUI(gui, player);
                });
    }

    public abstract TriggerType getTriggerType();
}
