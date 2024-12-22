package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LootGUI extends InventoryGUI {

    private static final Map<Integer, Consumer<Loot>> consumerMap = new HashMap<>();
    private static final Map<Integer, Boolean> editModeMap = new HashMap<>();
    private static final Map<Integer, ItemStack> dropMap = new HashMap<>();
    private static final Map<Integer, Float> chanceMap = new HashMap<>();
    private static final Map<Integer, Boolean> activeMap = new HashMap<>();
    private static final Map<Integer, Integer> minAmountMap = new HashMap<>();
    private static final Map<Integer, Integer> maxAmountMap = new HashMap<>();
    private static final Map<Integer, Boolean> applyFortuneMap = new HashMap<>();
    private static final Map<Integer, Boolean> dropWithSilkTouchMap = new HashMap<>();
    private static final Map<Integer, Boolean> eraseVanillaMap = new HashMap<>();

    public LootGUI() {
        this(false);

    }

    public LootGUI(boolean editMode) {
        super();
        editModeMap.put(getId(), editMode);
        chanceMap.put(getId(), 0.5f);
        activeMap.put(getId(), true);
        minAmountMap.put(getId(), 1);
        maxAmountMap.put(getId(), 1);
        applyFortuneMap.put(getId(), false);
        dropWithSilkTouchMap.put(getId(), false);
        eraseVanillaMap.put(getId(), false);
    }

    @Override
    public void init(Player player) {
        addButton(0, returnButton());
        addButton(11, addDropItemButton());
        super.init(player);
    }

    public ItemStack getDropItem() {
        return dropMap.get(getId());
    }

    public boolean isEditMode() {
        return editModeMap.get(getId());
    }

    public float getChance() {
        return chanceMap.get(getId());
    }

    public boolean isActive() {
        return activeMap.get(getId());
    }

    public int getMinAmount() {
        return minAmountMap.get(getId());
    }

    public int getMaxAmount() {
        return maxAmountMap.get(getId());
    }

    public boolean getApplyFortune() {
        return applyFortuneMap.get(getId());
    }

    public boolean getDropWithSilkTouch() {
        return dropWithSilkTouchMap.get(getId());
    }

    public boolean getEraseVanilla() {
        return eraseVanillaMap.get(getId());
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

                    Logger.debugLog("Selected drop item: " + newItem);


                    dropMap.put(getId(), newItem);

                    reloadGui();
                });
    }

    public LootGUI consumer(Consumer<Loot> consumer) {
        consumerMap.put(getId(), consumer);
        return this;
    }

    public Consumer<Loot> getConsumer() {
        return consumerMap.get(getId());
    }

    public void save() {
        Loot loot = new Loot(
                getDropItem(),
                getChance(),
                isActive(),
                getMinAmount(),
                getMaxAmount(),
                getApplyFortune(),
                getDropWithSilkTouch(),
                getEraseVanilla()
        );

        getConsumer().accept(loot);
    }
}
