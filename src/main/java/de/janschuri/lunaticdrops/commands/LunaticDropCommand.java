package de.janschuri.lunaticdrops.commands;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.gui.MainGUI;
import de.janschuri.lunaticlib.LunaticCommand;
import de.janschuri.lunaticlib.LunaticLanguageConfig;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.common.command.AbstractLunaticCommand;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class LunaticDropCommand extends AbstractLunaticCommand {
    @Override
    public LunaticLanguageConfig getLanguageConfig() {
        return LunaticDrops.getLanguageConfig();
    }

    @Override
    public String getPermission() {
        return "lunaticdrops.command.drop";
    }

    @Override
    public String getName() {
        return "lunaticdrops";
    }

    @Override
    public List<LunaticCommand> getSubcommands() {
        return List.of(
                new BlockBreakCommand()
        );
    }

    @Override
    public boolean execute(Sender sender, String[] args) {

        if (args.length != 0) {
            final String subcommand = args[0];

            for (LunaticCommand sc : getSubcommands()) {
                if (checkIsSubcommand(sc, subcommand)) {
                    String[] newArgs = new String[args.length - 1];
                    System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                    return sc.execute(sender, newArgs);
                }
            }
        }

        PlayerSender player = (PlayerSender) sender;

        Player p = Bukkit.getPlayer(player.getUniqueId());

        GUIManager.openGUI(new MainGUI(), p);
        return true;

    }
}
