package de.janschuri.lunaticdrops.commands.drops.pandaeat;

import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.gui.ListDropGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.LunaticCommand;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PandaEat extends Subcommand {

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.pandaeat";
    }

    @Override
    public String getName() {
        return "panda_eat";
    }

    @Override
    public List<LunaticCommand> getSubcommands() {
        return List.of(
                new PandaEatCreate(),
                new PandaEatEdit()
        );
    }

    @Override
    public boolean execute(Sender sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage("§cYou don't have permission to do that.");
            return true;
        }

        if (!(sender instanceof PlayerSender player)) {
            sender.sendMessage("§cYou must be a player to do that.");
            return true;
        }

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

        Player p = Bukkit.getPlayer(player.getUniqueId());

        GUIManager.openGUI(new ListDropGUI(TriggerType.PANDA_EAT), p);
        return true;
    }
}
