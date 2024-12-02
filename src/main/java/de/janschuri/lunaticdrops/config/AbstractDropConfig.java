package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticlib.common.config.LunaticConfigImpl;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;
import java.util.Map;

public abstract class AbstractDropConfig extends LunaticConfigImpl  {

    public static final String customDropPath = "/customdrops";

    public AbstractDropConfig(String path, String exampleFile) {
        super(LunaticDrops.getDataDirectory(), customDropPath+path, exampleFile);
    }

    @Override
    public void load() {
        super.load();
    }

    protected ItemStack getItemStack(String key) {
        Map<String, Object> map = getMap(key);
        return ItemStack.deserialize(map);
    }
}
