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

    private String name;
    private ItemStack eatenItem;
    private boolean matchNBT = false;

    public PandaEatEditorGUI() {
        super();
    }

    public PandaEatEditorGUI(String name) {
        super();
        this.name = name;
    }

    public PandaEatEditorGUI(PandaEat pandaEat) {
        super(pandaEat);
        this.name = pandaEat.getName();
        this.eatenItem = pandaEat.getEatenItem();
        this.matchNBT = pandaEat.isMatchNBT();
    }

    @Override
    public void init(Player player) {
        addButton(11, createAddEatItemButton());

        super.init(player);
    }

    public String getName() {
        return name;
    }

    public ItemStack getEatenItem() {
        return eatenItem;
    }

    public Boolean isMatchNBT() {
        return matchNBT;
    }

    @Override
    protected boolean allowSave() {
        return getName() != null
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

                    eatenItem = newItem;

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

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.PANDA_EAT;
    }
}
