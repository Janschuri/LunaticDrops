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

    private final Integer id;
    private static final Map<Integer, ItemStack> eatItems = new HashMap<>();
    private static final Map<Integer, Boolean> matchNBT = new HashMap<>();

    public PandaEatEditorGUI(Player player, String name) {
        super(player, name);
        this.id = getId();
        matchNBT.putIfAbsent(id, false);

        decorate(player);
    }

    public PandaEatEditorGUI(Player player, PandaEat pandaEat) {
        super(player, pandaEat);
        this.id = getId();
        matchNBT.put(id, pandaEat.isMatchNBT());
        eatItems.put(id, pandaEat.getEatenItem());

        decorate(player);
    }

    public PandaEatEditorGUI(Player player, String name, Inventory inventory) {
        super(player, name, inventory);
        this.id = getId();
        matchNBT.putIfAbsent(id, false);

        decorate(player);
    }

    public PandaEatEditorGUI(Player player, PandaEat pandaEat, Inventory inventory) {
        super(player, pandaEat, inventory);
        this.id = getId();
        matchNBT.put(id, pandaEat.isMatchNBT());
        eatItems.put(id, pandaEat.getEatenItem());

        decorate(player);
    }

    @Override
    protected Map<InventoryButton, Integer> getButtons() {
        return Map.of(
                createAddEatItemButton(), 20
        );
    }

    @Override
    protected boolean allowSave() {
        return super.allowSave()
                && eatItems.get(id) != null
                && matchNBT.get(id) != null;
    }

    private InventoryButton createAddEatItemButton() {

        ItemStack item = eatItems.get(id) == null ? new ItemStack(Material.AIR) : eatItems.get(id);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!isEditMode()) {
                        return;
                    }

                    ItemStack cursorItem = event.getCursor();
                    if (cursorItem == null || cursorItem.getType() == Material.AIR) {
                        return;
                    }

                    ItemStack newItem = cursorItem.clone();
                    newItem.setAmount(1);

                    eatItems.put(id, newItem);

                    reloadGui();
                });
    }

    protected void save() {
        PandaEat pandaEat = new PandaEat(
                getName(),
                getDropItem(),
                getChance(),
                isActive(),
                eatItems.get(id),
                matchNBT.get(id)
        );

        pandaEat.save();
    }
}
