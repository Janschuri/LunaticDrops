package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.drops.PandaEat;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobKillEditorGUI extends EditorGUI {

    private static final Map<Integer, EntityType> mobTypes = new HashMap<>();

    public MobKillEditorGUI() {
        super();
    }

    public MobKillEditorGUI(MobKill mobKill) {
        super(mobKill);
        mobTypes.put(getId(), mobKill.getMobType());
    }

    private EntityType getMobType() {
        return mobTypes.get(getId());
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
                getMobType() != null ? ItemStackUtils.getSpawnEgg(getMobType()) :
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
                                mobTypes.put(getId(), entityType);

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
                getChance(),
                isActive(),
                getMobType()
        );

        if (mobKill.save()) {
            MobKill newMobKill = (MobKill) LunaticDrops.getDrop(TriggerType.MOB_KILL, mobKill.getMobType().name());
            GUIManager.openGUI(new MobKillEditorGUI(newMobKill), player);
        }
    }
}
