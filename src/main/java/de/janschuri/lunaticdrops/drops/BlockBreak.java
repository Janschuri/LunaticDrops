package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils.itemStackToMap;

public class BlockBreak extends CustomDrop {

    private final Material block;

    public BlockBreak(@NotNull List<Loot> loot, @NotNull Float chance, @NotNull Boolean active, Material block) {
        super(loot, chance, active);
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
                "name", getName(),
                "loot", lootMaps,
                "chance", chance,
                "active", active,
                "block", block.name()
        );
    }

    @Override
    protected TriggerType getTriggerType() {
        return TriggerType.BLOCK_BREAK;
    }

    @Override
    public ItemStack getDisplayItem() {
        return new ItemStack(getBlock());
    }

    public Material getBlock() {
        return block;
    }

    public boolean matches(BlockState blockState) {
        return this.block.equals(blockState.getType());
    }
}
