package de.janschuri.lunaticdrops.drops;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BlockBreak extends AbstractCustomDrop {

    public BlockBreak(@NotNull String name,@NotNull  ItemStack drop,@NotNull Float chance,@NotNull Boolean active) {
        super(name, drop, chance, active);
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
