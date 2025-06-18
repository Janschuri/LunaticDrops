package de.janschuri.lunaticdrops.gui.editor;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.DropEntityBreed;
import de.janschuri.lunaticdrops.drops.DropMobKill;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.SelectMobGUI;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class EditorGUIEntityBreed extends EditorGUI {

    private EntityType entityType;
    private String oldName = null;

    public EditorGUIEntityBreed() {
        super();
    }

    public EditorGUIEntityBreed(EntityType entityType) {
        super();
        this.entityType = entityType;
    }

    public EditorGUIEntityBreed(DropEntityBreed mobKill) {
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

                    SelectMobGUI selectMobGUI = new SelectMobGUI() {
                                @Override
                                public List<EntityType> getItems() {
                                    return super.getItems()
                                            .stream()
                                            .filter(entityType -> Breedable.class.isAssignableFrom(entityType.getEntityClass()))
                                            .toList();
                                }

                            }
                            .consumer(entityType -> {
                                this.entityType = entityType;

                                if (LunaticDrops.dropExists(TriggerType.ENTITY_BREED, entityType.name())) {
                                    GUIManager.openGUI(new EditorGUIEntityBreed((DropEntityBreed) LunaticDrops.getDrop(TriggerType.ENTITY_BREED, entityType.name())), player);
                                    return;
                                }

                                GUIManager.openGUI(this, player);
                            });

                    GUIManager.openGUI(selectMobGUI, player);
                });
    }

    @Override
    protected void save(Player player) {
        DropEntityBreed mobKill = new DropEntityBreed(
                getItems(),
                isActive(),
                getEntityType()
        );

        if (mobKill.save(oldName)) {
            DropEntityBreed entityBreed = (DropEntityBreed) LunaticDrops.getDrop(TriggerType.ENTITY_BREED, mobKill.getMobType().name());
            GUIManager.openGUI(new EditorGUIEntityBreed(entityBreed), player);
        }
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.ENTITY_BREED;
    }
}
