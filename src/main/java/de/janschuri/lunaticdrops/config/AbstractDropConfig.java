package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticlib.common.config.LunaticConfigImpl;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;
import java.util.Map;

public abstract class AbstractDropConfig extends LunaticConfigImpl  {

    public AbstractDropConfig(Path path) {
        super(path);
    }

    @Override
    public void load() {
        super.load();
    }

    protected ItemStack getItemStack(String key) {
        Map<String, Object> map = getMap(key);
        return ItemStack.deserialize(map);
    }

    public abstract CustomDrop getDrop();
}
