package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.loot.LootFlag;
import de.janschuri.lunaticdrops.loot.SingleLoot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.Reopenable;
import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import de.rapha149.signgui.exception.SignGUIVersionException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static de.janschuri.lunaticdrops.utils.Utils.formatChance;
import static de.janschuri.lunaticdrops.utils.Utils.parseEquation;

public class LootGUI extends InventoryGUI implements Reopenable {

    private boolean editMode = false;
    private double chance = 0.5f;
    private String chanceString = "0.5";
    private BiConsumer<SingleLoot, Boolean> consumer;
    private ItemStack dropItem;
    private boolean active = true;
    private int minAmount = 1;
    private int maxAmount = 1;
    private List<LootFlag> flags = new ArrayList<>();
    private final TriggerType triggerType;
    private SingleLoot singleLoot = null;

    public LootGUI(TriggerType triggerType) {
        super();
        this.triggerType = triggerType;
    }

    public LootGUI(TriggerType triggerType, SingleLoot singleLoot) {
        super();
        this.singleLoot = singleLoot;
        this.triggerType = triggerType;
        this.dropItem = singleLoot.getDisplayItem().clone();
        this.chance = singleLoot.getChance();
        this.chanceString = singleLoot.getChanceEquation();
        this.active = singleLoot.isActive();
        this.minAmount = singleLoot.getMinAmount();
        this.maxAmount = singleLoot.getMaxAmount();
        this.flags = new ArrayList<>(singleLoot.getFlags());
    }

    public LootGUI editMode(boolean editMode) {
        this.editMode = editMode;
        return this;
    }

    @Override
    public void init(Player player) {
        addButton(0, returnButton());
        addButton(11, addDropItemButton());

        addButton(14, chanceButton());

        addButton(29, minAmountButton());

        addButton(33, maxAmountButton());

        if (editMode) {
            addButton(17, saveButton());

            addButton(8, deleteButton());
            addButton(15, increaseChanceButton());
            addButton(13, decreaseChanceButton());

            addButton(17, saveButton());

            addButton(28, decreaseMinAmountButton());
            addButton(30, increaseMinAmountButton());

            addButton(32, decreaseMaxAmountButton());
            addButton(34, increaseMaxAmountButton());
        } else {
            addButton(17, editButton());
        }

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


                if (flags.contains(LootFlag.ONLY_FULL_GROWN)) {
                    flagButtons.add(onlyFullGrownButton());
                }

        for (int i = 0; i < flagButtons.size(); i++) {
            addButton(45 + i, flagButtons.get(i));
        }

        super.init(player);
    }

    @Override
    public NamespacedKey uniqueKey() {
        return MainGUI.UNIQUE_KEY;
    }

    public ItemStack getDropItem() {
        return dropItem == null ? null : dropItem.clone();
    }

    public double getChance() {
        return chance;
    }

    public String getChanceString() {
        return chanceString;
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
                    getConsumer().accept(singleLoot, editMode);
                });
    }

    private InventoryButton deleteButton() {
        ItemStack itemStack = new ItemStack(Material.FLINT_AND_STEEL);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§cDelete");
        itemStack.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> itemStack)
                .consumer(event -> {
                    getConsumer().accept(null, editMode);
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

                        player.setItemOnCursor(getDropItem());
                        return;
                    }

                    if (!editMode) {
                        return;
                    }

                    ItemStack newItem = cursorItem.clone();
                    newItem.setAmount(1);

                    Logger.debugLog("Selected drop item: " + newItem);


                    dropItem = newItem;

                    reloadGui();
                });
    }

    public LootGUI consumer(BiConsumer<SingleLoot, Boolean> consumer) {
        this.consumer = consumer;
        return this;
    }

    public BiConsumer<SingleLoot, Boolean> getConsumer() {
        return consumer;
    }

    public void save() {

        SingleLoot singleLoot = new SingleLoot(
                getDropItem(),
                getChanceString(),
                isActive(),
                getMinAmount(),
                getMaxAmount(),
                flags
        );

        getConsumer().accept(singleLoot, editMode);
    }

    private InventoryButton saveButton() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aSave");
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    save();
                });
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
                    if (!editMode) {
                        return;
                    }

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
                    if (!editMode) {
                        return;
                    }

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
                    if (!editMode) {
                        return;
                    }

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
                    if (!editMode) {
                        return;
                    }

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
                    if (!editMode) {
                        return;
                    }

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
                    if (!editMode) {
                        return;
                    }

                    if (flags.contains(LootFlag.FORCE_MAX_AMOUNT)) {
                        flags.remove(LootFlag.FORCE_MAX_AMOUNT);
                    } else {
                        flags.add(LootFlag.FORCE_MAX_AMOUNT);
                    }

                    reloadGui();
                });
    }

    private InventoryButton onlyFullGrownButton() {
        ItemStack item = flags.contains(LootFlag.ONLY_FULL_GROWN) ? new ItemStack(Material.WHEAT) : new ItemStack(Material.WHEAT_SEEDS);

        String displayName = "§bOnly full grown";

        ArrayList<String> lore = new ArrayList<>();
        lore.add("If enabled, the drop will only happen");
        lore.add("if the block is fully grown (only for ageable blocks)");

        ItemMeta meta = item.getItemMeta();

        if (flags.contains(LootFlag.ONLY_FULL_GROWN)) {
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
                    if (!editMode) {
                        return;
                    }

                    if (flags.contains(LootFlag.ONLY_FULL_GROWN)) {
                        flags.remove(LootFlag.ONLY_FULL_GROWN);
                    } else {
                        flags.add(LootFlag.ONLY_FULL_GROWN);
                    }

                    reloadGui();
                });
    }

    private void setChance(double chance) {
        this.chance = chance;
        this.chanceString = String.valueOf(chance);
    }

    private void setChanceString(String chanceString, double chance) {
        this.chanceString = chanceString;
        setChance(chance);
    }

    private InventoryButton chanceButton() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + formatChance(getChance()));
        List<String> lore = new ArrayList<>();
        lore.add(getChanceString());
        lore.add("Chance for the drop to happen");
        meta.setLore(lore);
        item.setItemMeta(meta);


        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!editMode) {
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();

                    player.closeInventory();

                    SignGUI gui = null;
                    try {
                        gui = SignGUI.builder()
                                .setType(Material.DARK_OAK_SIGN)
                                .setHandler((p, result) -> {
                                    StringBuilder newChance = new StringBuilder();
                                    for (int i = 0; i < 4; i++) {
                                        newChance.append(result.getLine(i));
                                    }

                                    return List.of(
                                            SignGUIAction.run(() ->{
                                                Bukkit.getScheduler().runTask(BukkitLunaticLib.getInstance(), () -> {
                                                    Logger.debugLog("New chance: " + newChance);

                                                    String newChanceString = newChance.toString();

                                                    boolean percent = newChanceString.contains("%");

                                                    if (percent) {
                                                        newChanceString = newChanceString.replace("%", "");
                                                    }

                                                    Double parsedChance = parseEquation(newChanceString);


                                                    if (parsedChance == null) {
                                                            player.sendMessage("Invalid number");
                                                    } else {
                                                        if (parsedChance < 0) {
                                                            player.sendMessage("Chance must be greater than 0");
                                                        } else if (parsedChance > 100) {
                                                            player.sendMessage("Chance must be less than 100");
                                                        } else {
                                                            if (percent) {
                                                                parsedChance /= 100;
                                                            }

                                                            setChanceString(newChance.toString(), parsedChance);
                                                        }
                                                    }

                                                    GUIManager.openGUI(this, player);
                                                });
                                            })
                                    );
                                })
                                .build();
                    } catch (SignGUIVersionException e) {
                        throw new RuntimeException(e);
                    }

                    gui.open(player);
                });
    }

    private InventoryButton increaseChanceButton() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + formatChance(getChance()));
        List<String> lore = new  ArrayList<>();
        lore.add(getChanceString());
        lore.add("Chance for the drop to happen");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!editMode) {
                        return;
                    }

                    if (processingClickEvent()) {
                        return;
                    }

                    ClickType clickType = event.getClick();

                    switch (clickType) {
                        case SHIFT_RIGHT:
                            increaseChance((Player) event.getWhoClicked(), 0.0001d);
                            break;
                        case RIGHT:
                            increaseChance((Player) event.getWhoClicked(), 0.001d);
                            break;
                        case LEFT:
                            increaseChance((Player) event.getWhoClicked(), 0.01d);
                            break;
                        case SHIFT_LEFT:
                            increaseChance((Player) event.getWhoClicked(), 0.1d);
                            break;
                    }
                });
    }

    private InventoryButton decreaseChanceButton() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + formatChance(getChance()));
        List<String> lore = new  ArrayList<>();
        lore.add(getChanceString());
        lore.add("Chance for the drop to happen");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!editMode) {
                        return;
                    }

                    if (processingClickEvent()) {
                        return;
                    }

                    ClickType clickType = event.getClick();

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

    private void decreaseChance(Player player, double amount) {
        double newChance = getChance() - amount;

        if (newChance < 0) {
            newChance = 0;
        }

        setChance(newChance);

        reloadGui();
    }

    private void increaseChance(Player player, double amount) {
        double newChance = getChance() + amount;

        if (newChance > 1) {
            newChance = 1;
        }

        setChance(newChance);

        reloadGui();
    }

    private InventoryButton minAmountButton() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("min. Amount: " + getMinAmount());
        List<String> lore = List.of("Minimum amount of the drop, if it happens");
        meta.setLore(lore);
        item.setItemMeta(meta);


        return new InventoryButton()
                .creator((player) -> item);
    }

    private InventoryButton increaseMinAmountButton() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("min. Amount: " + getMinAmount());
        List<String> lore = List.of("Minimum amount of the drop, if it happens");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!editMode) {
                        return;
                    }

                    if (processingClickEvent()) {
                        return;
                    }

                    ClickType clickType = event.getClick();

                    switch (clickType) {
                        case LEFT, RIGHT:
                            increaseMinAmount(1);
                            break;
                        case SHIFT_LEFT, SHIFT_RIGHT:
                            increaseMinAmount(10);
                            break;
                    }
                });
    }

    private InventoryButton decreaseMinAmountButton() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("min. Amount: " + getMinAmount());
        List<String> lore = List.of("Minimum amount of the drop, if it happens");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!editMode) {
                        return;
                    }

                    if (processingClickEvent()) {
                        return;
                    }

                    ClickType clickType = event.getClick();

                    switch (clickType) {
                        case LEFT, RIGHT:
                            decreaseMinAmount(1);
                            break;
                        case SHIFT_LEFT, SHIFT_RIGHT:
                            decreaseMinAmount(10);
                            break;
                    }
                });
    }

    private void decreaseMinAmount(int amount) {
        int newMinAmount = getMinAmount() - amount;

        if (newMinAmount < 1) {
            newMinAmount = 1;
        }

        minAmount = newMinAmount;

        reloadGui();
    }

    private void increaseMinAmount(int amount) {
        int newMinAmount = getMinAmount() + amount;

        if (newMinAmount > getMaxAmount()) {
            newMinAmount = getMaxAmount();
        }

        minAmount = newMinAmount;

        reloadGui();
    }

    private InventoryButton maxAmountButton() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("max. Amount: " + getMaxAmount());
        List<String> lore = List.of("Maximum amount of the drop, if it happens");
        meta.setLore(lore);
        item.setItemMeta(meta);


        return new InventoryButton()
                .creator((player) -> item);
    }

    private InventoryButton increaseMaxAmountButton() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("max. Amount: " + getMaxAmount());
        List<String> lore = List.of("Maximum amount of the drop, if it happens");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!editMode) {
                        return;
                    }

                    if (processingClickEvent()) {
                        return;
                    }

                    ClickType clickType = event.getClick();

                    switch (clickType) {
                        case LEFT, RIGHT:
                            increaseMaxAmount(1);
                            break;
                        case SHIFT_LEFT, SHIFT_RIGHT:
                            increaseMaxAmount(10);
                            break;
                    }
                });
    }

    private InventoryButton decreaseMaxAmountButton() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("max. Amount: " + getMaxAmount());
        List<String> lore = List.of("Maximum amount of the drop, if it happens");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!editMode) {
                        return;
                    }

                    if (processingClickEvent()) {
                        return;
                    }

                    ClickType clickType = event.getClick();

                    switch (clickType) {
                        case LEFT, RIGHT:
                            decreaseMaxAmount(1);
                            break;
                        case SHIFT_LEFT, SHIFT_RIGHT:
                            decreaseMaxAmount(10);
                            break;
                    }
                });
    }

    private void decreaseMaxAmount(int amount) {
        int newMaxAmount = getMaxAmount() - amount;

        if (newMaxAmount < getMinAmount()) {
            newMaxAmount = getMinAmount();
        }

        maxAmount = newMaxAmount;

        reloadGui();
    }

    private void increaseMaxAmount(int amount) {
        int newMaxAmount = getMaxAmount() + amount;

        if (newMaxAmount < getMinAmount()) {
            newMaxAmount = getMinAmount();
        }

        maxAmount = newMaxAmount;

        reloadGui();
    }
}
