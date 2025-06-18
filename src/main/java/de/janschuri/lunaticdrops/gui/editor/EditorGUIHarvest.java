package de.janschuri.lunaticdrops.gui.editor;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropHarvest;
import de.janschuri.lunaticdrops.gui.SelectHarvestGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditorGUIHarvest extends EditorGUI {

    private String oldName = null;
    private Material block;

    public EditorGUIHarvest() {
        super();
    }

    public EditorGUIHarvest(Material block) {
        super();
        this.block = block;
    }

    public EditorGUIHarvest(DropHarvest harvest) {
        super(harvest);
        this.oldName = harvest.getName();
        this.block = harvest.getBlock();
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

        ItemStack itemStack = new ItemStack(Material.DEAD_BUSH);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§dNo block selected");
        itemStack.setItemMeta(meta);

        Material blockMaterial = null;

        if (getBlockType() != null) {
            switch (getBlockType()) {
                case SWEET_BERRY_BUSH:
                    blockMaterial = Material.SWEET_BERRIES;
                    break;
                case CAVE_VINES_PLANT:
                    blockMaterial = Material.GLOW_BERRIES;
                    break;
                default:
            }
        }

        ItemStack item =
                blockMaterial != null ? new ItemStack(blockMaterial) :
                        itemStack;

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!isEditMode()) {
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();

                    SelectHarvestGUI selectHarvestGUI = new SelectHarvestGUI()
                            .consumer(block -> {
                                this.block = block;
                                GUIManager.openGUI(this, player);
                                reloadGui();
                            });

                    GUIManager.openGUI(selectHarvestGUI, player);
                });
    }

    protected void save(Player player) {
        DropHarvest harvest = new DropHarvest(
                getItems(),
                isActive(),
                getBlockType()
        );

        if (harvest.save(oldName)) {
            DropHarvest newHarvest = (DropHarvest) LunaticDrops.getDrop(TriggerType.HARVEST, harvest.getBlock().name());
            GUIManager.openGUI(new EditorGUIHarvest(harvest), player);
        }
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.HARVEST;
    }
}
