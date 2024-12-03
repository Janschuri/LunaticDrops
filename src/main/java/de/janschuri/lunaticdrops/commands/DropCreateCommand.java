package de.janschuri.lunaticdrops.commands;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.gui.PandaEatEditorGUI;
import de.janschuri.lunaticlib.LunaticLanguageConfig;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.common.command.AbstractLunaticCommand;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
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

        if (args.length == 0) {
            return true;
        }

        String name = args[0];

        PlayerSender player = (PlayerSender) sender;

        Player p = Bukkit.getPlayer(player.getUniqueId());

        GUIManager.openGUI(new PandaEatEditorGUI(p, name), p);
        return true;

    }
}
