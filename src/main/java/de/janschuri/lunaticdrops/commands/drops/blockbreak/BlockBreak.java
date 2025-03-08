package de.janschuri.lunaticdrops.commands.drops.blockbreak;

import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.commands.drops.LunaticDrops;
import de.janschuri.lunaticdrops.gui.ListDropGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.command.HasHelpCommand;
import de.janschuri.lunaticlib.common.command.HasParentCommand;
import de.janschuri.lunaticlib.common.command.HasSubcommands;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class BlockBreak extends Subcommand implements HasHelpCommand, HasSubcommands, HasParentCommand {

    private static final BlockBreak INSTANCE = new BlockBreak();
    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", INSTANCE.getDefaultHelpMessage("Show the BlockBreak help page."))
            .defaultMessage("de", INSTANCE.getDefaultHelpMessage("Zeige die BlockBreak Hilfe Seite."));
    private static final CommandMessageKey HELP_HEADER_MK = new LunaticCommandMessageKey(INSTANCE, "help_header")
            .defaultMessage("en", "BlockBreak-Help")
            .defaultMessage("de", "BlockBreak-Hilfe");

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.blockbreak";
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return Map.of(
                HELP_MK, getName()
        );
    }

    @Override
    public String getName() {
        return "block_break";
    }

    @Override
    public List<Command> getSubcommands() {
        return List.of(
                new BlockBreakCreate(),
                new BlockBreakEdit()
        );
    }

    @Override
    public boolean handleNoMatchingSubcommand(Sender sender, String[] args) {
        if (!(sender instanceof PlayerSender player)) {
            sender.sendMessage("§cYou must be a player to do that.");
            return true;
        }

        Player p = Bukkit.getPlayer(player.getUniqueId());

        GUIManager.openGUI(new ListDropGUI(TriggerType.BLOCK_BREAK), p);
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
