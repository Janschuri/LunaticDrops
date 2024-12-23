package de.janschuri.lunaticdrops.commands.drops.mobkill;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.gui.BlockBreakEditorGUI;
import de.janschuri.lunaticdrops.gui.MobKillEditorGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobKillEdit extends Subcommand {

    static List<EntityType> entities = Arrays.stream(EntityType.values())
            .filter((entity) -> !LunaticDrops.dropExists(TriggerType.MOB_KILL, entity.name()))
            .toList();

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

        MobKill mobKill = (MobKill) LunaticDrops.getDrop(TriggerType.MOB_KILL, entityName);

        Player p = Bukkit.getPlayer(player.getUniqueId());
        GUIManager.openGUI(new MobKillEditorGUI(mobKill), p);
        return true;
    }

    @Override
    public List<Map<String, String>> getParams() {

        Map<String, String> entityParams = new HashMap<>();

        for (EntityType entity : entities) {
            entityParams.put(entity.name(), getPermission());
        }

        return List.of(entityParams);
    }
}
