package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.utils.TriggerType;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils.itemStackToMap;

public class BlockBreak extends CustomDrop {

    private final Material block;

    public BlockBreak(@NotNull  ItemStack drop,@NotNull Float chance,@NotNull Boolean active, Material block) {
        super(drop, chance, active);
        this.block = block;
    }

    @Override
    public String getName() {
        return getBlock().name();
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of(
                "drop", itemStackToMap(drop),
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
