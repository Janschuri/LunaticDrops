package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.CustomDropPandaEat;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.common.config.LunaticConfigImpl;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.nio.file.Path;
import java.util.Map;

public class PandaEatDropConfig extends LunaticConfigImpl  {

    private static final String path = "customdrops/pandaeat/";

    public PandaEatDropConfig(Path dataDirectory) {
        super(dataDirectory, path, path+"example.yml");
    }

    public PandaEatDropConfig(Path dataDirectory, String file) {
        super(dataDirectory, path+file, path+"example.yml");
    }

    @Override
    public void load() {
        super.load();
    }

    public CustomDropPandaEat getDrop() {
        return new CustomDropPandaEat(
                getItemStack("drop"),
                getFloat("chance"),
                getBoolean("active"),
                getItemStack("eatenItem"),
                getBoolean("matchNBT")
        );
    }

    private ItemStack getItemStack(String key) {
        Map<String, Object> map = getMap(key);
        String type = ((String) map.getOrDefault("type", "STONE")).toUpperCase();

        Material material = Material.getMaterial(type);

        if (material == null) {
            Logger.errorLog("Material " + type + " not found");
            return new ItemStack(Material.STONE);
        }

        ItemStack itemStack = new ItemStack(material);

        ItemMeta itemMeta = itemStack.getItemMeta();

        return itemStack;
    }

    private void setItemStack(String key, ItemStack itemStack) {
        Map<String, Object> map = itemStack.serialize();
        setMap(key, map);
    }
}
