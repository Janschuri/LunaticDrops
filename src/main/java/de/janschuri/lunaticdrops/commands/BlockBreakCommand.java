package de.janschuri.lunaticdrops.commands;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.gui.BlockBreakEditorGUI;
import de.janschuri.lunaticdrops.gui.ListDropGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.LunaticLanguageConfig;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.common.command.AbstractLunaticCommand;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockBreakCommand extends AbstractLunaticCommand {

    static List<Material> blocks = Arrays.stream(Material.values())
            .filter(Material::isBlock)
            .toList();

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
        return "block_break";
    }

    @Override
    public boolean execute(Sender sender, String[] args) {

        PlayerSender player = (PlayerSender) sender;

        Player p = Bukkit.getPlayer(player.getUniqueId());


        if (args.length == 0) {

            GUIManager.openGUI(new ListDropGUI(TriggerType.BLOCK_BREAK), p);
            return true;
        }

        String modifier = args[0];

        if (modifier.equals("create")) {
            if (args.length < 2) {
                sender.sendMessage("Usage: /lunaticdrops block_break create <block>");
                return true;
            }

            String blockName = args[1];

            Material block = Material.matchMaterial(args[1]);

            if (block == null) {
                sender.sendMessage("Invalid block: " + args[1]);
                return true;
            }

            if (LunaticDrops.dropExists(TriggerType.BLOCK_BREAK, blockName)) {
                sender.sendMessage("Block already exists");
                return true;
            }

            GUIManager.openGUI(new BlockBreakEditorGUI(block), p);
            return true;
        }

        if (modifier.equals("edit")) {
            if (args.length < 2) {
                sender.sendMessage("Usage: /lunaticdrops block_break edit <block>");
                return true;
            }

            String blockName = args[1];

            Material block = Material.matchMaterial(args[1]);

            if (block == null) {
                sender.sendMessage("Invalid block: " + args[1]);
                return true;
            }

            if (!LunaticDrops.dropExists(TriggerType.BLOCK_BREAK, blockName)) {
                sender.sendMessage("Block does not exist");
                return true;
            }

            BlockBreak blockBreak = (BlockBreak) LunaticDrops.getDrop(TriggerType.BLOCK_BREAK, blockName);

            GUIManager.openGUI(new BlockBreakEditorGUI(blockBreak), p);
            return true;
        }


        sender.sendMessage("Usage: /lunaticdrops block_break create <block>");

        return true;

    }

    @Override
    public List<Map<String, String>> getParams() {

        Map<String, String> modifiers = Map.of(
                "create", getPermission(),
                "edit", getPermission()
        );

        Map<String, String> blockParams = new HashMap<>();

        for (Material block : blocks) {
            blockParams.put(block.name(), getPermission());
        }

        return List.of(modifiers, blockParams);

    }
}
