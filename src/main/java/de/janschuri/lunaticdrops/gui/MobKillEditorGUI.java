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

import java.util.HashMap;
import java.util.Map;

public class MobKillEditorGUI extends EditorGUI {

    private final Inventory inventory;
    private static final Map<Inventory, EntityType> mobTypes = new HashMap<>();

    public MobKillEditorGUI(Player player, String name) {
        super(player, name);
        this.inventory = getInventory();

        decorate(player);
    }

    public MobKillEditorGUI(Player player, MobKill mobKill) {
        super(player, mobKill);
        this.inventory = getInventory();

        decorate(player);
    }

    public MobKillEditorGUI(Player player, String name, Inventory inventory) {
        super(player, name, inventory);
        this.inventory = inventory;

        decorate(player);
    }

    public MobKillEditorGUI(Player player, MobKill mobKill, Inventory inventory) {
        super(player, mobKill, inventory);
        this.inventory = inventory;

        decorate(player);
    }

    private EntityType getMobType() {
        return mobTypes.get(inventory);
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

        ItemStack item =
                getMobType() != null ? ItemStackUtils.getSpawnEgg(getMobType()) :
                new ItemStack(Material.GHAST_SPAWN_EGG);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    SelectMobGUI selectMobGUI = new SelectMobGUI(getInventory())
                            .consumer(entityType -> {
                                mobTypes.put(getInventory(), entityType);

                                Logger.debugLog("Selected mob: " + entityType);
                                this.reloadGui(player);
                            })
                            ;

                    GUIManager.openGUI(selectMobGUI, player);
                });
    }

    protected void save() {
        MobKill mobKill = new MobKill(
                getName(),
                getDropItem(),
                getChance(),
                isActive(),
                getMobType()
        );

        mobKill.save();
    }
}
