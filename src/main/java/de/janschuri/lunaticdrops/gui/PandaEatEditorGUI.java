package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import de.rapha149.signgui.exception.SignGUIVersionException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PandaEatEditorGUI extends EditorGUI {

    private String name;
    private String oldName = null;
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
        this.oldName = pandaEat.getName();
        this.name = pandaEat.getName();
        this.eatenItem = pandaEat.getEatenItem();
        this.matchNBT = pandaEat.isMatchNBT();
    }

    @Override
    public void init(Player player) {
        addButton(11, createAddEatItemButton());
        addButton(15, nameButton());

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

        if (pandaEat.save(oldName)) {
            PandaEat newPandaEat = (PandaEat) LunaticDrops.getDrop(TriggerType.PANDA_EAT, pandaEat.getName());
            GUIManager.openGUI(new PandaEatEditorGUI(newPandaEat), player);
        }
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.PANDA_EAT;
    }

    private InventoryButton nameButton() {
        ItemStack item = new ItemStack(Material.DARK_OAK_SIGN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getName() == null ? "No name set" : getName());
        item.setItemMeta(meta);


        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!isEditMode()) {
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();

                    player.closeInventory();

                    SignGUI gui = null;
                    try {
                        gui = SignGUI.builder()
                                .setType(Material.DARK_OAK_SIGN)
                                .setLine(0, getName() == null ? "" : getName().substring(0, Math.min(getName().length(), 16)))
                                .setHandler((p, result) -> {
                                    StringBuilder newName = new StringBuilder();
                                    for (int i = 0; i < 4; i++) {
                                        newName.append(result.getLine(i));
                                    }

                                    return List.of(
                                            SignGUIAction.run(() ->{
                                                Bukkit.getScheduler().runTask(LunaticDrops.getInstance(), () -> {
                                                    Logger.debugLog("New chance: " + newName);

                                                    String newNameString = newName.toString();

                                                    if (newNameString.isEmpty()) {
                                                        player.sendMessage("§cName cannot be empty");
                                                    } else {
                                                        if (newNameString.length() > 16) {
                                                            newNameString = newNameString.substring(0, 16);
                                                        }

                                                        name = newNameString;

                                                        GUIManager.openGUI(this, player);
                                                    }
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
}
