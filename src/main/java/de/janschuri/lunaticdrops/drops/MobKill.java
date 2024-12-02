package de.janschuri.lunaticdrops.drops;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MobKill extends AbstractCustomDrop {

    public MobKill(@NotNull String name, @NotNull ItemStack itemStack, @NotNull Float chance, @NotNull Boolean active) {
        super(name, itemStack, chance, active);
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of(
                "name", name,
                "drop", drop.serialize(),
                "chance", chance,
                "active", active
        );
    }

    @Override
    protected String getDropType() {
        return "";
    }
}
