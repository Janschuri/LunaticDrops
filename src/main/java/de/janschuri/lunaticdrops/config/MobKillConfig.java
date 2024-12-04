package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.entity.EntityType;

import java.nio.file.Path;

public class MobKillConfig extends AbstractDropConfig {

    public MobKillConfig(Path path) {
        super(path);
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public MobKill getDrop() {
        try {
            return new MobKill(
                    getString("name"),
                    getItemStack("drop"),
                    getFloat("chance"),
                    getBoolean("active"),
                    getMobType("mob")
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
