package de.janschuri.lunaticdrops.commands.drops;

import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.commands.drops.blockbreak.BlockBreak;
import de.janschuri.lunaticdrops.commands.drops.harvest.Harvest;
import de.janschuri.lunaticdrops.commands.drops.mobkill.MobKill;
import de.janschuri.lunaticdrops.commands.drops.pandaeat.PandaEat;
import de.janschuri.lunaticdrops.gui.MainGUI;
import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.command.HasHelpCommand;
import de.janschuri.lunaticlib.common.command.HasSubcommands;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class LunaticDrops extends Subcommand implements HasSubcommands, HasHelpCommand {

    private static final LunaticDrops INSTANCE = new LunaticDrops();

    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", "&6/%command% %subcommand% &7- Show the LunaticDrops help page.")
            .defaultMessage("de", "&6/%command% %subcommand% &7- Zeige die LunaticDrops Hilfe Seite.");
    private static final CommandMessageKey HELP_HEADER_MK = new LunaticCommandMessageKey(INSTANCE, "help_header")
            .defaultMessage("en", "LunaticDrops-Help")
            .defaultMessage("de", "LunaticDrops-Hilfe");

    @Override
    public String getPermission() {
        return "lunaticdrops.admin";
    }

    @Override
    public String getName() {
        return "lunaticdrops";
    }

    @Override
    public List<Command> getSubcommands() {
        return List.of(
                new LunaticDropsReload(),
                new BlockBreak(),
                new MobKill(),
                new PandaEat(),
                new Harvest(),
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

        GUIManager.reopenGUI(new MainGUI(), p);
        return true;
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return Map.of(
                HELP_MK, getPermission()
        );
    }

    @Override
    public boolean isPrimaryCommand() {
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
}
