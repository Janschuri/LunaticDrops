package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.Drop;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.loot.LootTable;
import de.janschuri.lunaticdrops.loot.SingleLoot;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.common.config.LunaticConfig;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DropConfig extends LunaticConfig {

    private final String fileName;

    public DropConfig(Path path) {
        super(path, path.getFileName().toString());

        fileName = path.getFileName().toString().replace(".yml", "");
    }

    public String getFileName() {
        return fileName;
    }

    public void load() {
        super.load(null);
    }

    public Drop getDrop() {
        try {
            List<Loot> lootList = getLoot("loot");
            boolean active = getBoolean("active");

            if (lootList == null) {
                lootList = new ArrayList<>();
            }

            return new Drop(
                    lootList,
                    active
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected List<Loot> getLoot(String key) {
        List<Map<String, Object>> lootList = getMapList(key);

        if (lootList == null) {
            Logger.errorLog("Loot list not found in config");
            return null;
        }

        List<Loot> loot = new ArrayList<>();

        for (Map<String, Object> lootMap : lootList) {
            if (!lootMap.containsKey("type")) {
                Logger.errorLog("Loot type not found in config");
                return null;
            }

            String type = (String) lootMap.get("type");

            if (type.equals("single")) {
                loot.add(new SingleLoot().fromMap(lootMap));
            } else if (type.equals("table")) {
                loot.add(new LootTable().fromMap(lootMap));
            } else {
                Logger.errorLog("Unknown loot type in config");
                return null;
            }
        }

        return loot;
    }

    protected ItemStack getItemStack(String key) {
        Map<String, Object> itemMap = getMap(key);
        if (itemMap == null) {
            Logger.errorLog("Item not found in config");
            return null;
        }
        return ItemStackUtils.mapToItemStack(itemMap);
    }
}
