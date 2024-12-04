package de.janschuri.lunaticdrops;

import de.janschuri.lunaticdrops.commands.LunaticDropCommand;
import de.janschuri.lunaticdrops.config.AbstractDropConfig;
import de.janschuri.lunaticdrops.config.LanguageConfig;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.events.PandaEatTask;
import de.janschuri.lunaticdrops.listener.PandaEatDropItemListener;
import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.common.LunaticLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LunaticDrops extends JavaPlugin {

    private static LunaticDrops instance;
    public static boolean debug = true;
    private static Path dataDirectory;
    private static LanguageConfig languageConfig;

    private static Map<String, Map<String, CustomDrop>> customDrops = new HashMap<>();

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
            languageConfig = new LanguageConfig(dataDirectory, "en");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LunaticLib.getPlatform().registerCommand(instance, new LunaticDropCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig() throws IOException {

        for (DropType dropType : DropType.values()) {
            getInstance().saveResource(getCustomDropPath() + dropType.getConfigPath() + "/example.yml", true);

            Path dropPath = dataDirectory.resolve(getCustomDropPath() + dropType.getConfigPath());
            if (!Files.exists(dropPath)) {
                Files.createDirectories(dropPath);
            }

            customDrops.put(dropType.getConfigPath(), new HashMap<>());

            for (Path path : getFiles(dropPath)) {
                if (!path.toString().endsWith(".yml")) {
                    continue;
                }

                loadCustomDrop(dropType, path);
            }

            Logger.debugLog("Loaded " + customDrops.get(dropType.getConfigPath()).size() + " " + dropType.getConfigPath() + " drops");
        }
    }

    public static void loadCustomDrop(DropType dropType, Path path) {
        AbstractDropConfig config = dropType.getConfig(path);
        config.load();
        CustomDrop drop = config.getDrop();
        Logger.debugLog("Drop: " + drop);
        if (drop == null) {
            Logger.errorLog("Failed to load drop from " + path);
            return;
        }
        customDrops.get(dropType.getConfigPath()).put(drop.getName(), drop);
    }

    public static List<Path> getFiles(Path directory) throws IOException {
        try (Stream<Path> paths = Files.list(directory)) {
            return paths.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
    }

    public static Path getDataDirectory() {
        return dataDirectory;
    }

    public static LunaticDrops getInstance() {
        return instance;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static List<CustomDrop> getDrops(DropType dropType) {
        return new ArrayList<>(customDrops.getOrDefault(dropType.getConfigPath(), new HashMap<>(0)).values());
    }

    public static LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    public static String getCustomDropPath() {
        return "customdrops";
    }
}
