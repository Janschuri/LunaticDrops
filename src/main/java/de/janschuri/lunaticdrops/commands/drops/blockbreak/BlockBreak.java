package de.janschuri.lunaticdrops.commands.drops.blockbreak;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.commands.drops.LunaticDropsReload;
import de.janschuri.lunaticdrops.gui.BlockBreakEditorGUI;
import de.janschuri.lunaticdrops.gui.ListDropGUI;
import de.janschuri.lunaticdrops.gui.MainGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.LunaticCommand;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockBreak extends Subcommand {

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.blockbreak";
    }

    @Override
    public String getName() {
        return "block_break";
    }

    @Override
    public List<LunaticCommand> getSubcommands() {
        return List.of(
                new BlockBreakCreate(),
                new BlockBreakEdit()
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

        GUIManager.openGUI(new ListDropGUI(TriggerType.BLOCK_BREAK), p);
        return true;
    }
}
