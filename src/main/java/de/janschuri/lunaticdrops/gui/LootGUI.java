package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.loot.SingleLoot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.Consumer;

public class LootGUI extends InventoryGUI {

    private float chance = 0.5f;
    private Consumer<SingleLoot> consumer;
    private ItemStack dropItem;
    private boolean active = true;
    private int minAmount = 1;
    private int maxAmount = 1;
    private boolean applyFortune = false;
    private boolean dropWithSilkTouch = false;
    private boolean eraseVanilla = false;

    public LootGUI(boolean editMode) {
        super();
    }

    public LootGUI(SingleLoot singleLoot) {
        super();
        this.dropItem = singleLoot.getItem();
        this.chance = singleLoot.getChance();
        this.active = singleLoot.isActive();
        this.minAmount = singleLoot.getMinAmount();
        this.maxAmount = singleLoot.getMaxAmount();
        this.applyFortune = singleLoot.getApplyFortune();
        this.dropWithSilkTouch = singleLoot.getDropWithSilkTouch();
        this.eraseVanilla = singleLoot.isEraseVanillaDrops();
    }

    @Override
    public void init(Player player) {
        addButton(0, returnButton());
        addButton(11, addDropItemButton());

        addButton(15, increaseChanceButton());
        addButton(14, chanceButton());
        addButton(13, decreaseChanceButton());

        addButton(17, saveButton());


        super.init(player);
    }

    public ItemStack getDropItem() {
        return dropItem;
    }

    public float getChance() {
        return chance;
    }

    public boolean isActive() {
        return active;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public boolean getApplyFortune() {
        return applyFortune;
    }

    public boolean getDropWithSilkTouch() {
        return dropWithSilkTouch;
    }

    public boolean getEraseVanilla() {
        return eraseVanilla;
    }

    private InventoryButton returnButton() {
        ItemStack itemStack = new ItemStack(Material.ARROW);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§cReturn");
        itemStack.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> itemStack)
                .consumer(event -> {
                    getConsumer().accept(null);
                });
    }

    private InventoryButton addDropItemButton() {

        ItemStack item = getDropItem() == null ? new ItemStack(Material.AIR) : getDropItem();

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {

                    Player player = (Player) event.getWhoClicked();

                    ItemStack cursorItem = event.getCursor();
                    if (cursorItem == null || cursorItem.getType() == Material.AIR) {
                        return;
                    }

                    ItemStack newItem = cursorItem.clone();
                    newItem.setAmount(1);

                    Logger.debugLog("Selected drop item: " + newItem);


                    dropItem = newItem;

                    reloadGui();
                });
    }

    public LootGUI consumer(Consumer<SingleLoot> consumer) {
        this.consumer = consumer;
        return this;
    }

    public Consumer<SingleLoot> getConsumer() {
        return consumer;
    }

    public void save() {
        SingleLoot singleLoot = new SingleLoot(
                getDropItem(),
                getChance(),
                isActive(),
                getMinAmount(),
                getMaxAmount(),
                getApplyFortune(),
                getDropWithSilkTouch(),
                getEraseVanilla()
        );

        getConsumer().accept(singleLoot);
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

        chance = newChance;

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

        chance = newChance;

        reloadGui();
    }

    private InventoryButton saveButton() {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.LIME_STAINED_GLASS_PANE))
                .consumer(event -> {
                    save();
                });
    }
}
