package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Material;
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

    @Override
    public BlockBreak getDrop() {
        try {
            return new BlockBreak(
                    getItemStack("drop"),
                    getFloat("chance"),
                    getBoolean("active"),
                    getMaterial("block")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Material getMaterial(String key) {
        String material = getString(key);
        try {
            return Material.valueOf(material);
        } catch (IllegalArgumentException e) {
            Logger.errorLog("Invalid material: " + material);
            return null;
        }
    }
}
