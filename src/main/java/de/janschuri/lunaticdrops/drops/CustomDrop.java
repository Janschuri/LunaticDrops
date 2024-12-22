package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class CustomDrop {

    protected final float chance;
    protected final Random random = new Random();
    protected final List<Loot> loot;
    protected final boolean active;

    public CustomDrop(@NotNull List<Loot> loot, @NotNull Float chance, @NotNull Boolean active) {
        this.chance = chance;
        this.loot = loot;
        this.active = active;
    }

    public abstract String getName();

    public float getChance() {
        return chance;
    }

    public boolean isActive() {
        return active;
    }

    public List<Loot> getLoot() {
        return loot;
    }

    public final boolean save() {
        Map<String, Object> data = toMap();

        String name = (String) data.get("name");

        File file = new File( LunaticDrops.getDataDirectory() + "/" + LunaticDrops.getCustomDropPath() + getTriggerType().getConfigPath() + "/" + name + ".yml");

        Logger.debugLog("Saving to: " + file.getAbsolutePath());

        Yaml yaml = new Yaml();

        String output = yaml.dump(data);

        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(output.getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
            LunaticDrops.updateDrop(getTriggerType(), this);
            return true;
    }

    public abstract Map<String, Object> toMap();

    protected abstract TriggerType getTriggerType();

    public abstract ItemStack getDisplayItem();
}
