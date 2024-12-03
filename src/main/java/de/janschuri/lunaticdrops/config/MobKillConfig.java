package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.drops.PandaEat;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;
import java.util.Map;

public class MobKillConfig extends AbstractDropConfig {

    public MobKillConfig(Path path) {
        super(path);
    }

    @Override
    public void load() {
        super.load();
    }

    public MobKill getDrop() {
        return null;
    }
}
