package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;
import java.util.Map;

public class PandaEatConfig extends AbstractDropConfig {

    private static final String path = "/pandaeat";

    public PandaEatConfig(Path path) {
        super(path);
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public PandaEat getDrop() {
        try {
            return new PandaEat(
                    getString("name"),
                    getItemStack("drop"),
                    getFloat("chance"),
                    getBoolean("active"),
                    getItemStack("eatenItem"),
                    getBoolean("matchNBT")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected ItemStack getItemStack(String key) {
        Map<String, Object> map = getMap(key);

        Logger.debugLog("Map: " + map);

        return ItemStackUtils.mapToItemStack(map);
    }

    public static String getPath() {
        return path;
    }
}
