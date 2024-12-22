package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.entity.EntityType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
            List<Loot> lootList = getLoot("loot");
            boolean active = getBoolean("active");
            EntityType mob = getMobType("mob");

            if (lootList == null) {
                lootList = new ArrayList<>();
            }

            return new MobKill(
                    lootList,
                    active,
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
