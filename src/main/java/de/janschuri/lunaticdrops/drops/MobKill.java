package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils.itemStackToMap;

public class MobKill extends CustomDrop {

    private final EntityType mobType;

    public MobKill(@NotNull List<Loot> loot, @NotNull Float chance, @NotNull Boolean active, @NotNull EntityType mobType) {
        super(loot, chance, active);
        this.mobType = mobType;
    }

    @Override
    public String getName() {
        return mobType.name();
    }

    @Override
    public Map<String, Object> toMap() {
        List<Map<String, Object>> lootMaps = getLoot().stream().map(Loot::toMap).toList();

        return Map.of(
                "name", getName(),
                "loot", lootMaps,
                "chance", chance,
                "active", active,
                "mob", mobType.name()
        );
    }

    @Override
    protected TriggerType getTriggerType() {
        return TriggerType.MOB_KILL;
    }

    @Override
    public ItemStack getDisplayItem() {
        return ItemStackUtils.getSpawnEgg(mobType);
    }

    public EntityType getMobType() {
        return mobType;
    }

    public boolean matches(EntityType entityType) {
        return mobType.equals(entityType);
    }
}
