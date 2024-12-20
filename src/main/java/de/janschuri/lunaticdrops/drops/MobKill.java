package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils.itemStackToMap;

public class MobKill extends CustomDrop {

    private final EntityType mobType;

    public MobKill(@NotNull ItemStack itemStack, @NotNull Float chance, @NotNull Boolean active, @NotNull EntityType mobType) {
        super(itemStack, chance, active);
        this.mobType = mobType;
    }

    @Override
    public String getName() {
        return mobType.name();
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of(
                "drop", itemStackToMap(drop),
                "chance", chance,
                "active", active,
                "mob", mobType.name()
        );
    }

    @Override
    protected DropType getDropType() {
        return DropType.MOB_KILL;
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
