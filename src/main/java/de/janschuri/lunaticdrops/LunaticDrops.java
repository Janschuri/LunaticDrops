package de.janschuri.lunaticdrops;

import de.janschuri.lunaticdrops.listener.PandaEatListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaticDrops extends JavaPlugin {

    private static LunaticDrops instance;
    public static boolean debug = true;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        getServer().getPluginManager().registerEvents(new PandaEatListener(), this);
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
