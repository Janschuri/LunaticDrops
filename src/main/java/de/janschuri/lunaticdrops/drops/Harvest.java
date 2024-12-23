package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class Harvest extends CustomDrop {

    private final Material block;

    public Harvest(@NotNull List<Loot> loot, @NotNull Boolean active, Material block) {
        super(loot, active);
        this.block = block;
    }

    @Override
    public String getName() {
        return getBlock().name();
    }

    @Override
    public Map<String, Object> toMap() {
        List<Map<String, Object>> lootMaps = getLoot().stream().map(Loot::toMap).toList();

        return Map.of(
                "loot", lootMaps,
                "active", active,
                "block", block.name()
        );
    }

    @Override
    protected TriggerType getTriggerType() {
        return TriggerType.HARVEST;
    }

    @Override
    public ItemStack getDisplayItem() {
        if (block == null) {
            return new ItemStack(Material.DEAD_BUSH);
        }

        if (block == Material.CAVE_VINES_PLANT) {
            return new ItemStack(Material.GLOW_BERRIES);
        }

        if (block == Material.SWEET_BERRY_BUSH) {
            return new ItemStack(Material.SWEET_BERRIES);
        }

        return new ItemStack(Material.DEAD_BUSH);
    }

    public Material getBlock() {
        return block;
    }
}
