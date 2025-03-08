package de.janschuri.lunaticdrops.commands.drops.pandaeat;

import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.commands.drops.LunaticDrops;
import de.janschuri.lunaticdrops.gui.ListDropGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.command.HasHelpCommand;
import de.janschuri.lunaticlib.common.command.HasParentCommand;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class PandaEat extends Subcommand implements HasHelpCommand, HasParentCommand {

    private static final PandaEat INSTANCE = new PandaEat();
    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", INSTANCE.getDefaultHelpMessage("Show the PandaEat help page."))
            .defaultMessage("de", INSTANCE.getDefaultHelpMessage("Zeige die PandaEat Hilfe Seite."));
    private static final CommandMessageKey HELP_HEADER_MK = new LunaticCommandMessageKey(INSTANCE, "help_header")
            .defaultMessage("en", "PandaEat-Help")
            .defaultMessage("de", "PandaEat-Hilfe");

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.pandaeat";
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return Map.of(
                HELP_MK, getPermission()
        );
    }

    @Override
    public String getName() {
        return "panda_eat";
    }

    @Override
    public List<Command> getSubcommands() {
        return List.of(
                new PandaEatCreate(),
                new PandaEatEdit(),
                getHelpCommand()
        );
    }

    @Override
    public boolean handleNoMatchingSubcommand(Sender sender, String[] args) {
        if (!(sender instanceof PlayerSender player)) {
            sender.sendMessage(getMessage(NO_CONSOLE_COMMAND_MK));
            return true;
        }

        Player p = Bukkit.getPlayer(player.getUniqueId());

        GUIManager.openGUI(new ListDropGUI(TriggerType.PANDA_EAT), p);
        return true;
    }

    @Override
    public MessageKey pageParamName() {
        return PAGE_MK;
    }

    @Override
    public MessageKey getHelpHeader() {
        return HELP_HEADER_MK;
    }

    @Override
    public Command getParentCommand() {
        return new LunaticDrops();
    }
}
