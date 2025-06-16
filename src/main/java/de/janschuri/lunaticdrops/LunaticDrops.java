package de.janschuri.lunaticdrops;

import de.janschuri.lunaticdrops.config.AbstractDropConfig;
import de.janschuri.lunaticdrops.config.LanguageConfig;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.events.PandaEatTask;
import de.janschuri.lunaticdrops.listener.*;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.common.LunaticLib;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
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
    public static final NamespacedKey PLACED_BY_PLAYER_KEY = new NamespacedKey("lunaticdrops", "placed_by_player");

    @Override
    public void onEnable() {
        getConfig().getItemStack("test");
        dataDirectory = getDataFolder().toPath();

        // Plugin startup logic
        instance = this;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PandaEatTask(), 0L, 20L); // Check every second (20 ticks)
        Bukkit.getPluginManager().registerEvents(new PandaEatDropItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new MobKillListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new HarvestListener(), this);

        languageConfig = new LanguageConfig(dataDirectory, "en");
            if (!loadConfig()) {
                Logger.errorLog("Error loading config");
            }

        LunaticLib.getPlatform().registerCommand(instance, new de.janschuri.lunaticdrops.commands.drops.LunaticDrops());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static boolean loadConfig() {

        boolean success = true;

        languageConfig.load();

        for (TriggerType dropType : TriggerType.values()) {

            Path dropPath = dataDirectory.resolve(getCustomDropPath() + dropType.getConfigPath());
            try {
                if (!Files.exists(dropPath)) {
                    Files.createDirectories(dropPath);
                }
            } catch (IOException e) {
                Logger.errorLog("Error creating directory " + dropPath);
                e.printStackTrace();
                return false;
            }

            List<Path> files = new ArrayList<>();
            try {
                files = getFiles(dropPath);
            } catch (IOException e) {
                Logger.errorLog("Error getting files from " + dropPath);
                e.printStackTrace();
                return false;
            }

            customDrops.put(dropType.getConfigPath(), new HashMap<>());

            for (Path path : files) {
                if (!path.toString().endsWith(".yml")) {
                    continue;
                }

                AbstractDropConfig config = dropType.getConfig(path);
                config.load();

                CustomDrop drop = config.getDrop();

                if (drop == null) {
                    Logger.errorLog("Error loading drop from " + path);
                    success = false;
                    continue;
                }

                customDrops.get(dropType.getConfigPath()).put(drop.getName(), drop);
            }

            Logger.debugLog("Loaded " + customDrops.get(dropType.getConfigPath()).size() + " " + dropType.getConfigPath() + " drops");
        }
        return true;
    }

    public static void updateDrop(TriggerType dropType, CustomDrop drop) {
        customDrops.get(dropType.getConfigPath()).put(drop.getName(), drop);
    }

    public static boolean dropExists(TriggerType dropType, String name) {
        if (!customDrops.containsKey(dropType.getConfigPath())) {
            return false;
        }

        return customDrops.get(dropType.getConfigPath()).containsKey(name);
    }

    public void removeDrop(TriggerType dropType, String name) {
        customDrops.get(dropType.getConfigPath()).remove(name);
    }

    public static CustomDrop getDrop(TriggerType dropType, String name) {
        return customDrops.get(dropType.getConfigPath()).get(name);
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

    public static List<CustomDrop> getDrops(TriggerType dropType) {
        return new ArrayList<>(customDrops.getOrDefault(dropType.getConfigPath(), new HashMap<>(0)).values());
    }

    public static LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    public static String getCustomDropPath() {
        return "customdrops";
    }
}
