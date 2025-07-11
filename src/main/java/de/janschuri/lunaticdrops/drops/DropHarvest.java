package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class DropHarvest extends Drop {

    private final Material block;

    public DropHarvest(@NotNull List<Loot> loot, boolean active, Material block) {
        super(loot, active);
        this.block = block;
    }

    public DropHarvest(Drop drop, Material block) {
        super(drop);
        this.block = block;
    }

    @Override
    public String getName() {
        return getBlock().name();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("block", getBlock().name());
        return map;
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
