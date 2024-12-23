package de.janschuri.lunaticdrops.commands.drops.pandaeat;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.gui.MobKillEditorGUI;
import de.janschuri.lunaticdrops.gui.PandaEatEditorGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PandaEatCreate extends Subcommand {

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.pandaeat.create";
    }

    @Override
    public String getName() {
        return "create";
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

        if (args.length < 1) {
            sender.sendMessage("Usage: /lunaticdrops panda_eat create <entity>");
            return true;
        }

        String name = args[0];

        if (LunaticDrops.dropExists(TriggerType.PANDA_EAT, name)) {
            sender.sendMessage("Panda eat drop already exists: " + name);
            return true;
        }

        Player p = Bukkit.getPlayer(player.getUniqueId());

        GUIManager.openGUI(new PandaEatEditorGUI(name), p);
        return true;
    }
}
