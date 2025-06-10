package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class Harvest extends CustomDrop {

    private final Material block;

    public Harvest(@NotNull List<Loot> loot, boolean active, Material block) {
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
    public TriggerType getTriggerType() {
        return TriggerType.HARVEST;
    }

    @Override
    public ItemStack getDisplayItem() {
        return ItemStackUtils.getItemStack(getBlock());
    }

    public Material getBlock() {
        return block;
    }
}
