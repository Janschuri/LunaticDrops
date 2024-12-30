package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.Harvest;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HarvestEditorGUI extends EditorGUI {

    private String oldName = null;
    private Material block;

    public HarvestEditorGUI() {
        super();
    }

    public HarvestEditorGUI(Material block) {
        super();
        this.block = block;
    }

    public HarvestEditorGUI(Harvest harvest) {
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
        Harvest harvest = new Harvest(
                getItems(),
                isActive(),
                getBlockType()
        );

        if (harvest.save(oldName)) {
            Harvest newHarvest = (Harvest) LunaticDrops.getDrop(TriggerType.HARVEST, harvest.getBlock().name());
            GUIManager.openGUI(new HarvestEditorGUI(harvest), player);
        }
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.HARVEST;
    }
}
