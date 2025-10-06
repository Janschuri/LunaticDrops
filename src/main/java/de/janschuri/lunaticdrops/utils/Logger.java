package de.janschuri.lunaticdrops.utils;


import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticlib.utils.LunaticLogger;

public class Logger {

    private static LunaticLogger logger = LunaticLogger.getLogger(LunaticDrops.getInstance().getName());

    public static boolean isDebug() {
        return LunaticDrops.isDebug();
    }

    public static void debug(String msg) {
        if (isDebug()) {
            logger.debug(msg);
        }
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warn(String msg) {
        logger.warn(msg);
    }

    public static void error(String msg) {
        logger.error(msg);
    }

}
