package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.Drop;
import de.janschuri.lunaticdrops.drops.DropEntityBreed;
import de.janschuri.lunaticdrops.drops.DropMobKill;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.entity.EntityType;

import java.nio.file.Path;

public class DropConfigEntityBreed extends DropConfig {

    public DropConfigEntityBreed(Path path) {
        super(path);
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public DropEntityBreed getDrop() {
        try {
            Drop drop = super.getDrop();
            EntityType mob = getMobType("mob");

            return new DropEntityBreed(
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
