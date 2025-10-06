package de.janschuri.lunaticdrops.commands;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticlib.commands.LunaticCommand;
import de.janschuri.lunaticlib.config.LunaticLanguageConfig;
import de.janschuri.lunaticlib.config.LunaticMessageKey;
import de.janschuri.lunaticlib.config.MessageKey;
import de.janschuri.lunaticlib.sender.Sender;
import net.kyori.adventure.text.Component;

public abstract class Subcommand extends LunaticCommand {

    protected static final MessageKey NO_CONSOLE_COMMAND_MK = new LunaticMessageKey("no_console_command")
            .defaultMessage("en", "This command can only be executed by a player.")
            .defaultMessage("de", "Dieser Befehl kann nur von einem Spieler ausgeführt werden.");

    protected static final MessageKey NO_PERMISSION_MK = new LunaticMessageKey("no_permission")
            .defaultMessage("en", "You don't have permission to execute this command.")
            .defaultMessage("de", "Du hast keine Berechtigung, diesen Befehl auszuführen.");

    protected static final MessageKey WRONG_USAGE_MK = new LunaticMessageKey("wrong_usage")
            .defaultMessage("en", "Wrong usage! Please check the command syntax.")
            .defaultMessage("de", "Falsche Verwendung! Bitte überprüfe die Befehlsyntax.");

    protected static final MessageKey PAGE_MK = new LunaticMessageKey("page")
            .defaultMessage("en", "Page")
            .defaultMessage("de", "Seite");

    protected static final MessageKey BLOCK_MK = new LunaticMessageKey("block")
            .defaultMessage("en", "Block")
            .defaultMessage("de", "Block");

    protected static final MessageKey MOB_MK = new LunaticMessageKey("mob")
            .defaultMessage("en", "Mob")
            .defaultMessage("de", "Mob");

    protected static final MessageKey NAME_MK = new LunaticMessageKey("name")
            .defaultMessage("en", "Name")
            .defaultMessage("de", "Name");

    @Override
    public LunaticLanguageConfig getLanguageConfig() {
        return LunaticDrops.getLanguageConfig();
    }

    @Override
    public Component noPermissionMessage(Sender sender, String[] strings) {
        return getMessage(NO_PERMISSION_MK);
    }

    @Override
    public Component wrongUsageMessage(Sender sender, String[] strings) {
        return getMessage(WRONG_USAGE_MK);
    }
}
