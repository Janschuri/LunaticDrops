package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ListDropGUI extends ListGUI<CustomDrop> {

    private TriggerType dropType;

    public ListDropGUI(TriggerType dropType) {
        super();
        this.dropType = dropType;
    }

    @Override
    public void init(Player player) {
        addButton(0, returnButton());
        addButton(8, addDropButton());
        super.init(player);
    }

    @Override
    public InventoryButton listItemButton(CustomDrop drop) {
        return new InventoryButton()
                .creator((player) -> drop.getDisplayItem())
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    GUIManager.openGUI(dropType.getEditorGUI(drop).inventory(getInventory()), player);
                });
    }

    @Override
    public List<CustomDrop> getItems() {
        return LunaticDrops.getDrops(dropType);
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
                    GUIManager.openGUI(new MainGUI().inventory(getInventory()), player);
                });
    }

    private InventoryButton addDropButton() {
        ItemStack itemStack = ItemStackUtils.getSkullFromURL("http://textures.minecraft.net/texture/5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1");

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§aAdd new drop");
        itemStack.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> itemStack)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    GUIManager.openGUI(dropType.getEditorGUI().inventory(getInventory()), player);
                });
    }
}