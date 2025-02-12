package de.janschuri.lunaticdrops.commands.drops;

import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.commands.drops.blockbreak.BlockBreak;
import de.janschuri.lunaticdrops.commands.drops.harvest.Harvest;
import de.janschuri.lunaticdrops.commands.drops.mobkill.MobKill;
import de.janschuri.lunaticdrops.commands.drops.pandaeat.PandaEat;
import de.janschuri.lunaticdrops.gui.MainGUI;
import de.janschuri.lunaticlib.LunaticCommand;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class LunaticDrops extends Subcommand {

    @Override
    public String getPermission() {
        return "lunaticdrops.admin";
    }

    @Override
    public String getName() {
        return "lunaticdrops";
    }

    @Override
    public List<LunaticCommand> getSubcommands() {
        return List.of(
                new LunaticDropsReload(),
                new BlockBreak(),
                new MobKill(),
                new PandaEat(),
                new Harvest()
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
