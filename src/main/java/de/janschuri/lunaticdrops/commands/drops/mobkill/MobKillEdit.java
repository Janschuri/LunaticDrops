package de.janschuri.lunaticdrops.commands.drops.mobkill;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.drops.DropMobKill;
import de.janschuri.lunaticdrops.gui.editor.EditorGUIMobKill;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.command.HasParams;
import de.janschuri.lunaticlib.common.command.HasParentCommand;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public class MobKillEdit extends Subcommand implements HasParentCommand, HasParams {

    private static final MobKillEdit INSTANCE = new MobKillEdit();
    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", INSTANCE.getDefaultHelpMessage("Edit the mob kill drop."))
            .defaultMessage("de", INSTANCE.getDefaultHelpMessage("Bearbeite den Mob-Kill-Drop."));

    private static List<EntityType> entities;

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.mobkill.edit";
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
            sender.sendMessage("Usage: /lunaticdrops mob_kill edit <entity>");
            return true;
        }

        String entityName = args[0];

        EntityType entity = EntityType.valueOf(entityName);

        if (entity.getEntityClass() == null) {
            sender.sendMessage("Invalid block: " + entityName);
            return true;
        }

        if (!LunaticDrops.dropExists(TriggerType.MOB_KILL, entityName)) {
            sender.sendMessage("Block does not exist");
            return true;
        }

        DropMobKill mobKill = (DropMobKill) LunaticDrops.getDrop(TriggerType.MOB_KILL, entityName);

        Player p = Bukkit.getPlayer(player.getUniqueId());
        GUIManager.openGUI(new EditorGUIMobKill(mobKill), p);
        return true;
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return Map.of();
    }

    @Override
    public List<MessageKey> getParamsNames() {
        return List.of(
                    PAGE_MK
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
        return new de.janschuri.lunaticdrops.commands.drops.mobkill.MobKill();
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
