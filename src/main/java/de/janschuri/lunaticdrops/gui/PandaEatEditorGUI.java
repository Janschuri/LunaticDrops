package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PandaEatEditorGUI extends EditorGUI {

    private final Inventory inventory;
    private static final Map<Inventory, ItemStack> eatItems = new HashMap<>();
    private static final Map<Inventory, Boolean> matchNBT = new HashMap<>();

    public PandaEatEditorGUI(String name) {
        super(name);
        this.inventory = getInventory();
        matchNBT.putIfAbsent(inventory, false);
    }

    public PandaEatEditorGUI(PandaEat pandaEat) {
        super(pandaEat);
        this.inventory = getInventory();
        matchNBT.put(inventory, pandaEat.isMatchNBT());
        eatItems.put(inventory, pandaEat.getEatenItem());
    }

    @Override
    protected Map<InventoryButton, Integer> getButtons() {
        return Map.of(
                createAddEatItemButton(), 20
        );
    }

    public ItemStack getEatenItem() {
        return eatItems.get(inventory);
    }

    public Boolean isMatchNBT() {
        return matchNBT.get(inventory);
    }

    @Override
    protected boolean allowSave() {
        return super.allowSave()
                && getEatenItem() != null
                && isMatchNBT() != null;
    }

    private InventoryButton createAddEatItemButton() {

        ItemStack item = getEatenItem() == null ? new ItemStack(Material.AIR) : getEatenItem();

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

                    eatItems.put(inventory, newItem);

                    reloadGui(player);
                });
    }

    protected void save() {
        PandaEat pandaEat = new PandaEat(
                getName(),
                getDropItem(),
                getChance(),
                isActive(),
                getEatenItem(),
                isMatchNBT()
        );

        pandaEat.save();
    }
}
