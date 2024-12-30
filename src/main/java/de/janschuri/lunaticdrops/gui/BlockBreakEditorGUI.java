package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.SelectBlockGUI;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockBreakEditorGUI extends EditorGUI {

    private Material block = null;
    private String oldName = null;

    public BlockBreakEditorGUI() {
        super();
    }

    public BlockBreakEditorGUI(Material block) {
        super();
        this.block = block;
    }

    public BlockBreakEditorGUI(BlockBreak blockBreak) {
        super(blockBreak);
        this.oldName = blockBreak.getName();
        this.block = blockBreak.getBlock();
    }

    private Material getBlockType() {
        return block;
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

        ItemStack item = getBlockType() != null ? ItemStackUtils.getItemStack(getBlockType()) : itemStack;

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!isEditMode()) {
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();

                    SelectBlockGUI selectBlockGUI = new SelectBlockGUI()
                            .consumer(block -> {
                                this.block = block;

                                if (LunaticDrops.dropExists(TriggerType.BLOCK_BREAK, block.name())) {
                                    Logger.debugLog("Mob kill drop already exists for " + block.name());
                                    GUIManager.openGUI(new BlockBreakEditorGUI((BlockBreak) LunaticDrops.getDrop(TriggerType.BLOCK_BREAK, block.name())), player);
                                    return;
                                }

                                GUIManager.openGUI(this, player);
                                reloadGui();
                            });

                    GUIManager.openGUI(selectBlockGUI, player);
                });
    }

    protected void save(Player player) {
        BlockBreak blockBreak = new BlockBreak(
                getItems(),
                isActive(),
                getBlockType()
        );

        if (blockBreak.save(oldName)) {
            BlockBreak newBlockBreak = (BlockBreak) LunaticDrops.getDrop(TriggerType.BLOCK_BREAK, blockBreak.getBlock().name());
            GUIManager.openGUI(new BlockBreakEditorGUI(newBlockBreak), player);
        }
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.BLOCK_BREAK;
    }
}
