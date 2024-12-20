package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Random;

public abstract class CustomDrop {

    protected final float chance;
    protected final Random random = new Random();
    protected final ItemStack drop;
    protected final boolean active;

    public CustomDrop(@NotNull  ItemStack drop, @NotNull Float chance, @NotNull Boolean active) {
        this.chance = chance;
        this.drop = drop;
        this.active = active;
    }

    public abstract String getName();

    public float getChance() {
        return chance;
    }

    public boolean isLucky() {
        float randomValue = random.nextFloat(0, 1); // Generates a float between 0.0 (inclusive) and 1.0 (exclusive)
        Logger.debugLog("Random value: " + randomValue + " Chance: " + chance);
        return randomValue <= chance;
    }

    public boolean isActive() {
        return active;
    }

    public ItemStack getDrop() {
        return drop;
    }

    public final boolean save() {
        Map<String, Object> data = toMap();

        String name = (String) data.get("name");

        File file = new File( LunaticDrops.getDataDirectory() + "/" + LunaticDrops.getCustomDropPath() + getDropType().getConfigPath() + "/" + name + ".yml");

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
            LunaticDrops.updateDrop(getDropType(), this);
            return true;
    }

    public abstract Map<String, Object> toMap();

    protected abstract DropType getDropType();

    public abstract ItemStack getDisplayItem();
}
