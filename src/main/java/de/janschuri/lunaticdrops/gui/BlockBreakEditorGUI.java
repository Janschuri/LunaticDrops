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

    private final Inventory inventory;
    private static final Map<Inventory, Material> blockTypes = new HashMap<>();

    public BlockBreakEditorGUI(Player player, String name) {
        super(player, name);
        this.inventory = getInventory();

        decorate(player);
    }

    public BlockBreakEditorGUI(Player player, BlockBreak blockBreak) {
        super(player, blockBreak);
        this.inventory = getInventory();
        blockTypes.put(inventory, blockBreak.getBlock());

        decorate(player);
    }

    public BlockBreakEditorGUI(Player player, String name, Inventory inventory) {
        super(player, name, inventory);
        this.inventory = inventory;

        decorate(player);
    }

    public BlockBreakEditorGUI(Player player, BlockBreak blockBreak, Inventory inventory) {
        super(player, blockBreak, inventory);
        this.inventory = inventory;
        blockTypes.put(inventory, blockBreak.getBlock());

        decorate(player);
    }

    private Material getBlockType() {
        return blockTypes.get(inventory);
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

                    blockTypes.put(inventory, newItem.getType());

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
