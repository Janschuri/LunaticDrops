package de.janschuri.lunaticdrops.commands.drops.pandaeat;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.drops.Drop;
import de.janschuri.lunaticdrops.drops.DropPandaEat;
import de.janschuri.lunaticdrops.gui.editor.EditorGUIPandaEat;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.command.HasParams;
import de.janschuri.lunaticlib.common.command.HasParentCommand;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PandaEatEdit extends Subcommand implements HasParams, HasParentCommand {

    private static final PandaEatEdit INSTANCE = new PandaEatEdit();
    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", INSTANCE.getDefaultHelpMessage("Edit the panda eat drop."))
            .defaultMessage("de", INSTANCE.getDefaultHelpMessage("Bearbeite den Panda-Eat-Drop."));

    static List<String> pandaEatDrops;

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

        DropPandaEat pandaEat = (DropPandaEat) LunaticDrops.getDrop(TriggerType.PANDA_EAT, name);

        if (pandaEat == null) {
            sender.sendMessage("Panda eat drop not found: " + name);
            return true;
        }

        Player p = Bukkit.getPlayer(player.getUniqueId());
        GUIManager.openGUI(new EditorGUIPandaEat(pandaEat), p);
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
        Map<String, String> params = new HashMap<>();

        for (String drop : getPandaEatDrops()) {
            params.put(drop, getPermission());
        }

        return List.of(params);
    }

    @Override
    public Command getParentCommand() {
        return new de.janschuri.lunaticdrops.commands.drops.pandaeat.PandaEat();
    }

    private List<String> getPandaEatDrops() {
        if (pandaEatDrops == null) {
            pandaEatDrops = LunaticDrops.getDrops(TriggerType.PANDA_EAT).stream()
                    .map(Drop::getName)
                    .toList();
        }
        return pandaEatDrops;
    }
}
