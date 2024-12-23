package de.janschuri.lunaticdrops.commands.drops.pandaeat;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.gui.PandaEatEditorGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PandaEatEdit extends Subcommand {

    static List<String> pandaEatDrops = LunaticDrops.getDrops(TriggerType.PANDA_EAT).stream()
            .map(CustomDrop::getName)
            .toList();

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.pandaeat.edit";
    }

    @Override
    public String getName() {
        return "edit";
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
            sender.sendMessage("Usage: /lunaticdrops panda_eat edit <name>");
            return true;
        }

        String name = args[0];

        PandaEat pandaEat = (PandaEat) LunaticDrops.getDrop(TriggerType.PANDA_EAT, name);

        if (pandaEat == null) {
            sender.sendMessage("Panda eat drop not found: " + name);
            return true;
        }

        Player p = Bukkit.getPlayer(player.getUniqueId());
        GUIManager.openGUI(new PandaEatEditorGUI(pandaEat), p);
        return true;
    }

    @Override
    public List<Map<String, String>> getParams() {
        Map<String, String> params = new HashMap<>();

        for (String drop : pandaEatDrops) {
            params.put(drop, getPermission());
        }

        return List.of(params);
    }
}
