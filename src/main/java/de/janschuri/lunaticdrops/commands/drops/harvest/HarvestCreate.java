package de.janschuri.lunaticdrops.commands.drops.harvest;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.gui.BlockBreakEditorGUI;
import de.janschuri.lunaticdrops.gui.HarvestEditorGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HarvestCreate extends Subcommand {

    static List<Material> blocks = Arrays.asList(
            Material.SWEET_BERRY_BUSH,
            Material.CAVE_VINES_PLANT
    ).stream().filter(
            material -> !LunaticDrops.dropExists(TriggerType.BLOCK_BREAK, material.name())
    ).toList();

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.harvest.create";
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
            sender.sendMessage("Usage: /lunaticdrops harvest create <block>");
            return true;
        }

        String blockName = args[0];

        Material block = Material.matchMaterial(blockName);

        if (block == null) {
            sender.sendMessage("Invalid block: " + blockName);
            return true;
        }

        if (!blocks.contains(block)) {
            sender.sendMessage("Block not allowed");
            return true;
        }

        if (LunaticDrops.dropExists(TriggerType.HARVEST, blockName)) {
            sender.sendMessage("Block already exists");
            return true;
        }

        Player p = Bukkit.getPlayer(player.getUniqueId());

        GUIManager.openGUI(new HarvestEditorGUI(block), p);
        return true;
    }

    @Override
    public List<Map<String, String>> getParams() {

        Map<String, String> blockParams = new HashMap<>();

        for (Material block : blocks) {
            blockParams.put(block.name(), getPermission());
        }

        return List.of(blockParams);
    }
}
