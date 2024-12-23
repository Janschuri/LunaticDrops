package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.loot.LootFlag;
import de.janschuri.lunaticdrops.loot.SingleLoot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LootGUI extends InventoryGUI {

    private float chance = 0.5f;
    private Consumer<SingleLoot> consumer;
    private ItemStack dropItem;
    private boolean active = true;
    private int minAmount = 1;
    private int maxAmount = 1;
    private List<LootFlag> flags = new ArrayList<>();
    private TriggerType triggerType;

    public LootGUI(TriggerType triggerType, boolean editMode) {
        super();
        this.triggerType = triggerType;
    }

    public LootGUI(TriggerType triggerType, SingleLoot singleLoot) {
        super();
        this.triggerType = triggerType;
        this.dropItem = singleLoot.getItem();
        this.chance = singleLoot.getChance();
        this.active = singleLoot.isActive();
        this.minAmount = singleLoot.getMinAmount();
        this.maxAmount = singleLoot.getMaxAmount();
        this.flags = singleLoot.getFlags();
    }

    @Override
    public void init(Player player) {
        addButton(0, returnButton());
        addButton(11, addDropItemButton());

        addButton(15, increaseChanceButton());
        addButton(14, chanceButton());
        addButton(13, decreaseChanceButton());

        addButton(17, saveButton());

        List<LootFlag> flags = LootFlag.getFlags(triggerType);

        List<InventoryButton> flagButtons = new ArrayList<>();

        if (flags.contains(LootFlag.APPLY_FORTUNE)) {
            flagButtons.add(applyFortuneButton());
        }

        if (flags.contains(LootFlag.APPLY_LOOTING)) {
            flagButtons.add(applyLootingButton());
        }

        if (flags.contains(LootFlag.DROP_ONLY_TO_PLAYER)) {
            flagButtons.add(dropOnlyToPlayerButton());
        }

        if (flags.contains(LootFlag.DROP_WITH_SILK_TOUCH)) {
            flagButtons.add(dropWithSilkTouchButton());
        }

        if (flags.contains(LootFlag.ERASE_VANILLA_DROPS)) {
            flagButtons.add(eraseVanillaButton());
        }

        if (flags.contains(LootFlag.FORCE_MAX_AMOUNT)) {
            flagButtons.add(forceMaxAmountButton());
        }

        for (int i = 0; i < flagButtons.size(); i++) {
            addButton(45 + i, flagButtons.get(i));
        }

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
                flags
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

    private InventoryButton eraseVanillaButton() {
        ItemStack item = flags.contains(LootFlag.ERASE_VANILLA_DROPS) ? new ItemStack(Material.BARRIER) : new ItemStack(Material.STRUCTURE_VOID);

        String displayName = "§bErase vanilla drops";

        ArrayList<String> lore = new ArrayList<>();
        lore.add("If enabled, vanilla drops will be erased");
        lore.add("if the drop happens");

        ItemMeta meta = item.getItemMeta();

        if (flags.contains(LootFlag.ERASE_VANILLA_DROPS)) {
            assert meta != null;
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            lore.add("§aEnabled");
        } else {
            lore.add("§cDisabled");
        }

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (flags.contains(LootFlag.ERASE_VANILLA_DROPS)) {
                        flags.remove(LootFlag.ERASE_VANILLA_DROPS);
                    } else {
                        flags.add(LootFlag.ERASE_VANILLA_DROPS);
                    }

                    reloadGui();
                });
    }

    private InventoryButton dropWithSilkTouchButton() {
        ItemStack item = flags.contains(LootFlag.DROP_WITH_SILK_TOUCH) ? new ItemStack(Material.ENCHANTED_BOOK) : new ItemStack(Material.BOOK);

        String displayName = "§bDrop with silk touch";

        List<String> lore = new ArrayList<>();
        lore.add("If enabled, the drop will be happen");
        lore.add("even if the block was mined with silk touch");

        ItemMeta meta = item.getItemMeta();

        if (flags.contains(LootFlag.DROP_WITH_SILK_TOUCH)) {
            assert meta != null;
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            lore.add("§aEnabled");
        } else {
            lore.add("§cDisabled");
        }

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (flags.contains(LootFlag.DROP_WITH_SILK_TOUCH)) {
                        flags.remove(LootFlag.DROP_WITH_SILK_TOUCH);
                    } else {
                        flags.add(LootFlag.DROP_WITH_SILK_TOUCH);
                    }

                    reloadGui();
                });
    }

    private InventoryButton dropOnlyToPlayerButton() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);


        String displayName = "§bDrop only to player";

        List<String> lore = new ArrayList<>();
        lore.add("If enabled, the drop will only happen");
        lore.add("if a player killed the mob");

        ItemMeta meta = item.getItemMeta();

        if (flags.contains(LootFlag.DROP_ONLY_TO_PLAYER)) {
            assert meta != null;
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            lore.add("§aEnabled");
        } else {
            lore.add("§cDisabled");
        }

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (flags.contains(LootFlag.DROP_ONLY_TO_PLAYER)) {
                        flags.remove(LootFlag.DROP_ONLY_TO_PLAYER);
                    } else {
                        flags.add(LootFlag.DROP_ONLY_TO_PLAYER);
                    }

                    reloadGui();
                });
    }

    private InventoryButton applyFortuneButton() {
        ItemStack item = flags.contains(LootFlag.APPLY_FORTUNE) ? new ItemStack(Material.ENCHANTED_BOOK) : new ItemStack(Material.BOOK);

        String displayName = "§bApply fortune";

        List<String> lore = new ArrayList<>();
        lore.add("If enabled, the drop will be affected");
        lore.add("by the fortune enchantment");

        ItemMeta meta = item.getItemMeta();

        if (flags.contains(LootFlag.APPLY_FORTUNE)) {
            assert meta != null;
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            lore.add("§aEnabled");
        } else {
            lore.add("§cDisabled");
        }

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (flags.contains(LootFlag.APPLY_FORTUNE)) {
                        flags.remove(LootFlag.APPLY_FORTUNE);
                    } else {
                        flags.add(LootFlag.APPLY_FORTUNE);
                    }

                    reloadGui();
                });
    }

    private InventoryButton applyLootingButton() {
        ItemStack item = flags.contains(LootFlag.APPLY_LOOTING) ? new ItemStack(Material.ENCHANTED_BOOK) : new ItemStack(Material.BOOK);

        String displayName = "§bApply looting";



        List<String> lore = new ArrayList<>();
        lore.add("If enabled, the drop will be affected");
        lore.add("by the looting enchantment");

        ItemMeta meta = item.getItemMeta();

        if (flags.contains(LootFlag.APPLY_LOOTING)) {
            assert meta != null;
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            lore.add("§aEnabled");
        } else {
            lore.add("§cDisabled");
        }
        meta.setDisplayName(displayName);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (flags.contains(LootFlag.APPLY_LOOTING)) {
                        flags.remove(LootFlag.APPLY_LOOTING);
                    } else {
                        flags.add(LootFlag.APPLY_LOOTING);
                    }

                    reloadGui();
                });
    }

    private InventoryButton forceMaxAmountButton() {
        ItemStack item = flags.contains(LootFlag.FORCE_MAX_AMOUNT) ? new ItemStack(Material.RED_NETHER_BRICK_WALL) : new ItemStack(Material.OAK_FENCE);

        String displayName = "§bForce max amount";

        ArrayList<String> lore = new ArrayList<>();
        lore.add("If enabled, the drop will be capped,");
        lore.add("even if fortune or looting would increase it");

        ItemMeta meta = item.getItemMeta();

        if (flags.contains(LootFlag.FORCE_MAX_AMOUNT)) {
            assert meta != null;
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            lore.add("§aEnabled");
        } else {
            lore.add("§cDisabled");
        }

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (flags.contains(LootFlag.FORCE_MAX_AMOUNT)) {
                        flags.remove(LootFlag.FORCE_MAX_AMOUNT);
                    } else {
                        flags.add(LootFlag.FORCE_MAX_AMOUNT);
                    }

                    reloadGui();
                });
    }
}
