package de.janschuri.lunaticdrops;

import de.janschuri.lunaticdrops.commands.LunaticDropCommand;
import de.janschuri.lunaticdrops.config.LanguageConfig;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticlib.common.LunaticLib;
import org.bukkit.Bukkit;
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
    private static LanguageConfig languageConfig;

    private static List<PandaEat> pandaEatDrops = new ArrayList<>();

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
//        Path pandaEatPath = dataDirectory.resolve("customdrops/pandaeat");
//
//        for (Path file : getFiles(pandaEatPath)) {
//            if (!file.toString().endsWith(".yml")) {
//                continue;
//            }
//            AbstractDropConfig config = new AbstractDropConfig(dataDirectory, file.getFileName().toString());
//            config.load();
//            PandaEat drop = config.getDrop();
//            pandaEatDrops.add(drop);
//        }
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

    public static List<PandaEat> getPandaEatDrops() {
        return pandaEatDrops;
    }

    public static LanguageConfig getLanguageConfig() {
        return languageConfig;
    }
}
