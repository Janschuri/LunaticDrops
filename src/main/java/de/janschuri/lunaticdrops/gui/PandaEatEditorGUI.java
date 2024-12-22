package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PandaEatEditorGUI extends EditorGUI {

    private final Inventory inventory;
    private static final Map<Inventory, ItemStack> eatItems = new HashMap<>();
    private static final Map<Inventory, Boolean> matchNBT = new HashMap<>();

    public PandaEatEditorGUI() {
        super();
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
    public void init(Player player) {
        addButton(11, createAddEatItemButton());

        super.init(player);
    }

    public String getName() {
        return "bamboo";
    }

    public ItemStack getEatenItem() {
        return eatItems.get(inventory);
    }

    public Boolean isMatchNBT() {
        return matchNBT.get(inventory);
    }

    @Override
    protected boolean allowSave() {
        return getEatenItem() != null
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

                    reloadGui();
                });
    }

    @Override
    protected void save(Player player) {
        PandaEat pandaEat = new PandaEat(
                getName(),
                getItems(),
                isActive(),
                getEatenItem(),
                isMatchNBT()
        );

        if (pandaEat.save()) {
            PandaEat newPandaEat = (PandaEat) LunaticDrops.getDrop(TriggerType.PANDA_EAT, pandaEat.getName());
            GUIManager.openGUI(new PandaEatEditorGUI(newPandaEat), player);
        }
    }
}
