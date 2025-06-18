package de.janschuri.lunaticdrops.commands.drops.pandaeat;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.gui.editor.EditorGUIPandaEat;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.command.HasParams;
import de.janschuri.lunaticlib.common.command.HasParentCommand;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class PandaEatCreate extends Subcommand implements HasParams, HasParentCommand {

    private static final PandaEatCreate INSTANCE = new PandaEatCreate();
    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", INSTANCE.getDefaultHelpMessage("Create a panda eat drop."))
            .defaultMessage("de", INSTANCE.getDefaultHelpMessage("Erstelle einen Panda-Eat-Drop."));
    private static final CommandMessageKey ALREADY_EXISTS_MK = new LunaticCommandMessageKey(INSTANCE, "already_exists")
            .defaultMessage("en", "Panda eat drop already exists: %name%")
            .defaultMessage("de", "Panda-Eat-Drop existiert bereits: %name%");

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
        if (!(sender instanceof PlayerSender player)) {
            sender.sendMessage(getMessage(NO_CONSOLE_COMMAND_MK));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(getMessage(WRONG_USAGE_MK));
            return true;
        }

        String name = args[0];

        if (LunaticDrops.dropExists(TriggerType.PANDA_EAT, name)) {
            sender.sendMessage(getMessage(ALREADY_EXISTS_MK, placeholder("%name%", name)));
            return true;
        }

        Player p = Bukkit.getPlayer(player.getUniqueId());

        GUIManager.openGUI(new EditorGUIPandaEat(name), p);
        return true;
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return Map.of(
            HELP_MK, getPermission()
        );
    }

    @Override
    public List<MessageKey> getParamsNames() {
        return List.of(
            NAME_MK
        );
    }

    @Override
    public List<Map<String, String>> getParams() {
        return List.of();
    }

    @Override
    public Command getParentCommand() {
        return new PandaEat();
    }
}
