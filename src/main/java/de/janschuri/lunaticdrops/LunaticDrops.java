package de.janschuri.lunaticdrops;

import de.janschuri.lunaticdrops.config.PandaEatDropConfig;
import de.janschuri.lunaticdrops.drops.CustomDropPandaEat;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LunaticDrops extends JavaPlugin {

    private static LunaticDrops instance;
    public static boolean debug = true;
    private static Path dataDirectory;

    private static List<CustomDropPandaEat> pandaEatDrops = new ArrayList<>();

    @Override
    public void onEnable() {
        getConfig().getItemStack("test");
        dataDirectory = getDataFolder().toPath();

        // Plugin startup logic
        instance = this;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PandaEatTask(), 0L, 20L); // Check every second (20 ticks)
        Bukkit.getPluginManager().registerEvents(new PandaEatDropItemListener(), this);

        try {
            loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig() throws IOException {
        Path pandaEatPath = dataDirectory.resolve("customdrops/pandaeat");

        for (Path file : getFiles(pandaEatPath)) {
            if (!file.toString().endsWith(".yml")) {
                continue;
            }
            PandaEatDropConfig config = new PandaEatDropConfig(dataDirectory, file.getFileName().toString());
            config.load();
            CustomDropPandaEat drop = config.getDrop();
            pandaEatDrops.add(drop);
        }
    }

    public static List<Path> getFiles(Path directory) throws IOException {
        try (Stream<Path> paths = Files.list(directory)) {
            return paths.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
    }

    public static LunaticDrops getInstance() {
        return instance;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static List<CustomDropPandaEat> getPandaEatDrops() {
        return pandaEatDrops;
    }
}
