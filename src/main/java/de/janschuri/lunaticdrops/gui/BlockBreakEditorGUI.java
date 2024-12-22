package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.SelectBlockGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class BlockBreakEditorGUI extends EditorGUI {

    private static final Map<Integer, Material> blockTypes = new HashMap<>();
    private static final Map<Integer, ItemStack> drops = new HashMap<>();

    public BlockBreakEditorGUI() {
        super();
    }

    public BlockBreakEditorGUI(Material block) {
        super();
        blockTypes.put(getId(), block);
    }

    public BlockBreakEditorGUI(BlockBreak blockBreak) {
        super(blockBreak);
        blockTypes.put(getId(), blockBreak.getBlock());
    }

    private Material getBlockType() {
        return blockTypes.get(getId());
    }

    @Override
    public void init(Player player) {
        addButton(11, selectBlockButton());

        super.init(player);
    }

    @Override
    protected boolean allowSave() {
        return getBlockType() != null;
    }

    private InventoryButton selectBlockButton() {

        ItemStack itemStack = new ItemStack(Material.STONE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§dNo block selected");
        itemStack.setItemMeta(meta);

        ItemStack item =
                getBlockType() != null ? new ItemStack(getBlockType()) :
                        itemStack;

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    SelectBlockGUI selectBlockGUI = new SelectBlockGUI()
                            .consumer(block -> {
                                blockTypes.put(getId(), block);

                                if (LunaticDrops.dropExists(TriggerType.BLOCK_BREAK, block.name())) {
                                    Logger.debugLog("Mob kill drop already exists for " + block.name());
                                    GUIManager.openGUI(new BlockBreakEditorGUI((BlockBreak) LunaticDrops.getDrop(TriggerType.BLOCK_BREAK, block.name())), player);
                                    return;
                                }

                                GUIManager.openGUI(new BlockBreakEditorGUI(block), player);
                            });

                    GUIManager.openGUI(selectBlockGUI, player);
                });
    }

    protected void save(Player player) {
    }
}
