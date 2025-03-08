package de.janschuri.lunaticdrops.commands.drops.mobkill;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.gui.BlockBreakEditorGUI;
import de.janschuri.lunaticdrops.gui.MobKillEditorGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.command.HasParams;
import de.janschuri.lunaticlib.common.command.HasParentCommand;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobKillCreate extends Subcommand implements HasParentCommand, HasParams {

    private static final MobKillCreate INSTANCE = new MobKillCreate();
    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", INSTANCE.getDefaultHelpMessage("Create a mob kill drop."))
            .defaultMessage("de", INSTANCE.getDefaultHelpMessage("Erstelle einen Mob-Kill-Drop."));

    static List<EntityType> entities = Arrays.stream(EntityType.values())
            .filter((entity) -> !LunaticDrops.dropExists(TriggerType.MOB_KILL, entity.name()))
            .toList();

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.mobkill.create";
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
            sender.sendMessage("Usage: /lunaticdrops mob_kill create <entity>");
            return true;
        }

        String mobName = args[0];

        EntityType entity = EntityType.valueOf(mobName);

        if (entity.getEntityClass() == null) {
            sender.sendMessage("Invalid entity: " + mobName);
            return true;
        }

        if (LunaticDrops.dropExists(TriggerType.MOB_KILL, mobName)) {
            sender.sendMessage("Mob kill drop already exists: " + mobName);
            return true;
        }

        Player p = Bukkit.getPlayer(player.getUniqueId());

        GUIManager.openGUI(new MobKillEditorGUI(entity), p);
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
               MOB_MK
        );
    }

    @Override
    public List<Map<String, String>> getParams() {

        Map<String, String> entityParams = new HashMap<>();

        for (EntityType entity : entities) {
            entityParams.put(entity.name(), getPermission());
        }

        return List.of(entityParams);
    }

    @Override
    public Command getParentCommand() {
        return new MobKill();
    }
}
