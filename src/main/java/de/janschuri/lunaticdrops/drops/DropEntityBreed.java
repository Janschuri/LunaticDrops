package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class DropEntityBreed extends Drop {

    private final EntityType mobType;

    public DropEntityBreed(@NotNull List<Loot> loot, boolean active, @NotNull EntityType mobType) {
        super(loot, active);
        this.mobType = mobType;
    }

    public DropEntityBreed(Drop drop, @NotNull EntityType mobType) {
        super(drop);
        this.mobType = mobType;
    }

    @Override
    public String getName() {
        return mobType.name();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("mob", mobType.name());
        return map;
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.ENTITY_BREED;
    }

    @Override
    public ItemStack getDisplayItem() {
        return ItemStackUtils.getSpawnEgg(mobType);
    }

    public EntityType getMobType() {
        return mobType;
    }
}
