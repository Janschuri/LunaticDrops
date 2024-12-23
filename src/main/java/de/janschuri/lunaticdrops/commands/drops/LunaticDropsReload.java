package de.janschuri.lunaticdrops.commands.drops;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticlib.Sender;

public class LunaticDropsReload extends Subcommand {


    @Override
    public String getPermission() {
        return "lunaticdrops.admin.reload";
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public boolean execute(Sender sender, String[] strings) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage("§cYou don't have permission to do that.");
            return true;
        }

        if (LunaticDrops.loadConfig()) {
            sender.sendMessage("§aConfig reloaded.");
        } else {
            sender.sendMessage("§cAn error occurred while reloading the config.");
        }

        return true;
    }
}
