package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.AbstractCustomDrop;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticlib.common.config.LunaticConfigImpl;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;
import java.util.Map;

public abstract class PandaEatConfig extends AbstractDropConfig {

    private static final String path = "/pandaeat";

    public PandaEatConfig(String file) {
        super(file, "example.yml");
    }

    @Override
    public void load() {
        super.load();
    }

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
        return ItemStack.deserialize(map);
    }

    public static String getPath() {
        return path;
    }
}
