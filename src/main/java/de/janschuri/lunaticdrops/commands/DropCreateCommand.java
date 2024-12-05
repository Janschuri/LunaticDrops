package de.janschuri.lunaticdrops.commands;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.gui.EditorGUI;
import de.janschuri.lunaticdrops.gui.MobKillEditorGUI;
import de.janschuri.lunaticdrops.gui.PandaEatEditorGUI;
import de.janschuri.lunaticdrops.utils.DropType;
import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticlib.LunaticLanguageConfig;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.common.command.AbstractLunaticCommand;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.SelectMobGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DropCreateCommand extends AbstractLunaticCommand {
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
        return "create";
    }

    @Override
    public boolean execute(Sender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("Usage: /lunaticdrops create <type> <name>");
            return true;
        }

        String type = args[0];
        String name = args[1];

        DropType dropType = DropType.fromString(type);

        if (dropType == null) {
            sender.sendMessage("Invalid drop type");
            return true;
        }

        PlayerSender player = (PlayerSender) sender;

        Player p = Bukkit.getPlayer(player.getUniqueId());

        Logger.debugLog("Creating drop of type " + dropType + " with name " + name);

        InventoryGUI gui = dropType.getEditorGUI(p, name);
        GUIManager.openGUI(gui, p);

        return true;
    }
}
