package de.janschuri.lunaticdrops;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaticDrops extends JavaPlugin {

    private static LunaticDrops instance;
    public static boolean debug = true;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PandaEatTask(), 0L, 20L); // Check every second (20 ticks)
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LunaticDrops getInstance() {
        return instance;
    }

    public static boolean isDebug() {
        return debug;
    }
}
