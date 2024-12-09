package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BlockBreakEditorGUI extends EditorGUI {

    private static final Map<Integer, Material> blockTypes = new HashMap<>();

    public BlockBreakEditorGUI(String name) {
        super(name);
    }

    public BlockBreakEditorGUI(BlockBreak blockBreak) {
        super(blockBreak);
        blockTypes.put(getId(), blockBreak.getBlock());
    }

    private Material getBlockType() {
        return blockTypes.get(getId());
    }

    @Override
    protected Map<InventoryButton, Integer> getButtons() {
        return Map.of(
                selectBlockButton(), 20
        );
    }

    @Override
    protected boolean allowSave() {
        return super.allowSave();
    }

    private InventoryButton selectBlockButton() {

        ItemStack item =
                getBlockType() != null ? new ItemStack(getBlockType()) :
                new ItemStack(Material.STONE);

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

                    if (!cursorItem.getType().isBlock()) {
                        player.sendMessage("§cYou can only select blocks!");
                        return;
                    }

                    ItemStack newItem = cursorItem.clone();
                    newItem.setAmount(1);

                    blockTypes.put(getId(), newItem.getType());

                    reloadGui(player);
                });
    }

    protected void save() {
        BlockBreak blockBreak = new BlockBreak(
                getName(),
                getDropItem(),
                getChance(),
                isActive(),
                getBlockType()
        );

        blockBreak.save();
    }
}
