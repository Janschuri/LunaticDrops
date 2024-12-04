package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.common.config.LunaticConfigImpl;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
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

    public abstract CustomDrop getDrop();

    protected ItemStack getItemStack(String key) {
        Map<String, Object> map = getMap(key);
        return ItemStackUtils.mapToItemStack(map);
    }
}
