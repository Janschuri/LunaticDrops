package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.Drop;
import de.janschuri.lunaticdrops.drops.DropMobKill;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.entity.EntityType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DropConfigMobKill extends DropConfig {

    public DropConfigMobKill(Path path) {
        super(path);
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public DropMobKill getDrop() {
        try {
            Drop drop = super.getDrop();
            EntityType mob = getMobType("mob");

            return new DropMobKill(
                    drop,
                    mob
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected EntityType getMobType(String key) {
        String mobType = getString(key);
        try {
            return EntityType.valueOf(mobType);
        } catch (IllegalArgumentException e) {
            Logger.errorLog("Invalid mob type: " + mobType);
            return null;
        }
    }
}
