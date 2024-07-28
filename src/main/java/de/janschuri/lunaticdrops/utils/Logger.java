package de.janschuri.lunaticdrops.utils;


import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticlib.common.logger.AbstractLogger;

public class Logger {

    private static String pluginName = LunaticDrops.getInstance().getName();

    public static boolean isDebug() {
        return LunaticDrops.isDebug();
    }

    public static void debugLog(String msg) {
        if (isDebug()) {
            AbstractLogger.debug(pluginName, msg);
        }
    }

    public static void infoLog(String msg) {
        AbstractLogger.info(pluginName, msg);
    }

    public static void warnLog(String msg) {
        AbstractLogger.warn(pluginName, msg);
    }

    public static void errorLog(String msg) {
        AbstractLogger.error(pluginName, msg);
    }

}
