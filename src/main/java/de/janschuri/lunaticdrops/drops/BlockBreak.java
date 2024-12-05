package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils.itemStackToMap;

public class BlockBreak extends CustomDrop {

    private final Material block;

    public BlockBreak(@NotNull String name,@NotNull  ItemStack drop,@NotNull Float chance,@NotNull Boolean active, Material block) {
        super(name, drop, chance, active);
        this.block = block;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of(
                "name", name,
                "drop", itemStackToMap(drop),
                "chance", chance,
                "active", active,
                "block", block.name()
        );
    }

    @Override
    protected DropType getDropType() {
        return DropType.BLOCK_BREAK;
    }

    public Material getBlock() {
        return block;
    }

    public boolean matches(BlockState blockState) {
        return this.block.equals(blockState.getType());
    }
}
