package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.utils.Logger;
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

    @Override
    public InventoryButton listItemButton(ItemStack itemStack) {
        ItemStack item = new ItemStack(Material.STONE);
        return new InventoryButton()
                .creator((player) -> item);
    }

    @Override
    public List<ItemStack> getItems() {
        return List.of();
    }

    public MobKillEditorGUI(MobKill mobKill) {
        super(mobKill);
        mobTypes.put(getId(), mobKill.getMobType());
    }

    private EntityType getMobType() {
        return mobTypes.get(getId());
    }

    @Override
    protected Map<InventoryButton, Integer> getButtons() {
        return Map.of(
                selectMobButton(), 20
        );
    }

    @Override
    protected boolean allowSave() {
        return super.allowSave();
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
                    Player player = (Player) event.getWhoClicked();

                    SelectMobGUI selectMobGUI = new SelectMobGUI(getId())
                            .consumer(entityType -> {
                                mobTypes.put(getId(), entityType);

                                Logger.debugLog("Selected mob: " + entityType);
                                this.reloadGui(player);
                            })
                            ;

                    GUIManager.openGUI(selectMobGUI, player);
                });
    }

    protected void save() {
        MobKill mobKill = new MobKill(
                getDropItem(),
                getChance(),
                isActive(),
                getMobType()
        );

        mobKill.save();
    }
}
