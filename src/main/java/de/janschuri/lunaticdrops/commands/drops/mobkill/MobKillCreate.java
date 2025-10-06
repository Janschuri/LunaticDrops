package de.janschuri.lunaticdrops.commands.drops.mobkill;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.gui.editor.EditorGUIMobKill;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.HasParams;
import de.janschuri.lunaticlib.commands.HasParentCommand;
import de.janschuri.lunaticlib.config.CommandMessageKey;
import de.janschuri.lunaticlib.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.paper.inventorygui.handler.GUIManager;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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

    static List<EntityType> entities;

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

        GUIManager.openGUI(new EditorGUIMobKill(entity), p);
        return true;
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return Map.of(
                HELP_MK, getPermission()
        );
    }

    @Override
    public List<Component> getParamsNames() {
        return List.of(
               getMessage(MOB_MK.noPrefix())
        );
    }

    @Override
    public List<Map<String, String>> getParams() {

        Map<String, String> entityParams = new HashMap<>();

        for (EntityType entity : getEntities()) {
            entityParams.put(entity.name(), getPermission());
        }

        return List.of(entityParams);
    }

    @Override
    public Command getParentCommand() {
        return new MobKill();
    }

    private List<EntityType> getEntities() {
        if (entities == null) {
            entities = Arrays.stream(EntityType.values())
                    .filter((entity) -> !LunaticDrops.dropExists(TriggerType.MOB_KILL, entity.name()))
                    .toList();
        }
        return entities;
    }
}
