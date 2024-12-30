package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.SelectMobGUI;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MobKillEditorGUI extends EditorGUI {

    private EntityType entityType;
    private String oldName = null;

    public MobKillEditorGUI() {
        super();
    }

    public MobKillEditorGUI(EntityType entityType) {
        super();
        this.entityType = entityType;
    }

    public MobKillEditorGUI(MobKill mobKill) {
        super(mobKill);
        this.entityType = mobKill.getMobType();
        this.oldName = mobKill.getName();
    }

    private EntityType getEntityType() {
        return entityType;
    }

    @Override
    public void init(Player player) {
        addButton(11, selectMobButton());

        super.init(player);
    }

    @Override
    protected boolean allowSave() {
        return true;
    }

    private InventoryButton selectMobButton() {

        ItemStack itemStack = new ItemStack(Material.GHAST_SPAWN_EGG);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§dNo mob selected");
        itemStack.setItemMeta(meta);

        ItemStack item =
                getEntityType() != null ? ItemStackUtils.getSpawnEgg(getEntityType()) :
                itemStack;

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (!isEditMode()) {
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();

                    SelectMobGUI selectMobGUI = new SelectMobGUI()
                            .consumer(entityType -> {
                                this.entityType = entityType;

                                if (LunaticDrops.dropExists(TriggerType.MOB_KILL, entityType.name())) {
                                    Logger.debugLog("Mob kill drop already exists for " + entityType.name());
                                    GUIManager.openGUI(new MobKillEditorGUI((MobKill) LunaticDrops.getDrop(TriggerType.MOB_KILL, entityType.name())), player);
                                    return;
                                }

                                GUIManager.openGUI(this, player);
                            });

                    GUIManager.openGUI(selectMobGUI, player);
                });
    }

    @Override
    protected void save(Player player) {
        List<Loot> lootList = getItems();

        MobKill mobKill = new MobKill(
                getItems(),
                isActive(),
                getEntityType()
        );

        if (mobKill.save(oldName)) {
            MobKill newMobKill = (MobKill) LunaticDrops.getDrop(TriggerType.MOB_KILL, mobKill.getMobType().name());
            GUIManager.openGUI(new MobKillEditorGUI(newMobKill), player);
        }
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.MOB_KILL;
    }
}
