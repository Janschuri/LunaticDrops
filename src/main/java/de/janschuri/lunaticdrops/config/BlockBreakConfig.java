package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.PandaEat;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;
import java.util.Map;

public class BlockBreakConfig extends AbstractDropConfig {

    public BlockBreakConfig(Path path) {
        super(path);
    }

    @Override
    public void load() {
        super.load();
    }

    public PandaEat getDrop() {
        return null;
    }
}
