package de.janschuri.lunaticdrops.gui.editor;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropBlockBreak;
import de.janschuri.lunaticdrops.drops.DropLeavesDecay;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.SelectBlockGUI;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EditorGUILeavesDecay extends EditorGUI {

    private Material block = null;
    private String oldName = null;

    public EditorGUILeavesDecay() {
        super();
    }

    public EditorGUILeavesDecay(Material block) {
        super();
        this.block = block;
    }

    public EditorGUILeavesDecay(DropLeavesDecay leavesDecay) {
        super(leavesDecay);
        this.oldName = leavesDecay.getName();
        this.block = leavesDecay.getBlock();
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

                    SelectBlockGUI selectBlockGUI = new SelectBlockGUI() {
                                @Override
                                public List<Material> getItems() {
                                    return super.getItems()
                                            .stream()
                                            .filter(material -> {
                                                BlockData blockData = material.createBlockData();
                                                return blockData instanceof org.bukkit.block.data.type.Leaves;
                                            })
                                            .toList();

                                }
                            }
                            .consumer(block -> {
                                this.block = block;

                                if (LunaticDrops.dropExists(TriggerType.LEAVES_DECAY, block.name())) {
                                    GUIManager.openGUI(new EditorGUILeavesDecay((DropLeavesDecay) LunaticDrops.getDrop(TriggerType.LEAVES_DECAY, block.name())), player);
                                    return;
                                }

                                GUIManager.openGUI(this, player);
                                reloadGui();
                            });

                    GUIManager.openGUI(selectBlockGUI, player);
                });
    }

    protected void save(Player player) {
        DropLeavesDecay blockBreak = new DropLeavesDecay(
                getItems(),
                isActive(),
                getBlockType()
        );

        if (blockBreak.save(oldName)) {
            DropLeavesDecay newBlockBreak = (DropLeavesDecay) LunaticDrops.getDrop(TriggerType.LEAVES_DECAY, blockBreak.getBlock().name());
            GUIManager.openGUI(new EditorGUILeavesDecay(newBlockBreak), player);
        }
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.LEAVES_DECAY;
    }
}
