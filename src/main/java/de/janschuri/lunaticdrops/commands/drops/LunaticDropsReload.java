package de.janschuri.lunaticdrops.commands.drops;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.Command;
import de.janschuri.lunaticlib.CommandMessageKey;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.common.command.HasParentCommand;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;

import java.util.Map;

public class LunaticDropsReload extends Subcommand implements HasParentCommand {

    private static final LunaticDropsReload INSTANCE = new LunaticDropsReload();

    private static final CommandMessageKey RELOADED_MK = new LunaticCommandMessageKey(INSTANCE, "reloaded")
            .defaultMessage("en", "Config reloaded.")
            .defaultMessage("de", "Konfiguration neu geladen.");

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.reload";
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public boolean execute(Sender sender, String[] strings) {
        if (LunaticDrops.loadConfig()) {
            sender.sendMessage(getMessage(RELOADED_MK));
        } else {
            Logger.errorLog("Config could not be reloaded.");
            return false;
        }

        return true;
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return Map.of(
                RELOADED_MK, getPermission()
        );
    }

    @Override
    public Command getParentCommand() {
        return new de.janschuri.lunaticdrops.commands.drops.LunaticDrops();
    }
}
