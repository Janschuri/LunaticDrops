package de.janschuri.lunaticdrops.commands.drops.blockbreak;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.gui.BlockBreakEditorGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.command.HasParams;
import de.janschuri.lunaticlib.common.command.HasParentCommand;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockBreakEdit extends Subcommand implements HasParams, HasParentCommand {

    private static final BlockBreakEdit INSTANCE = new BlockBreakEdit();
    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", INSTANCE.getDefaultHelpMessage("Edit the block break drop."))
            .defaultMessage("de", INSTANCE.getDefaultHelpMessage("Bearbeite den Blockbruch-Drop."));

    static List<Material> blocks;

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.blockbreak.edit";
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
            sender.sendMessage("Usage: /lunaticdrops block_break edit <block>");
            return true;
        }

        String blockName = args[0];

        Material block = Material.matchMaterial(blockName);

        if (block == null) {
            sender.sendMessage("Invalid block: " + blockName);
            return true;
        }

        if (!LunaticDrops.dropExists(TriggerType.BLOCK_BREAK, blockName)) {
            sender.sendMessage("Block does not exist");
            return true;
        }

        BlockBreak blockBreak = (BlockBreak) LunaticDrops.getDrop(TriggerType.BLOCK_BREAK, blockName);

        Player p = Bukkit.getPlayer(player.getUniqueId());
        GUIManager.openGUI(new BlockBreakEditorGUI(blockBreak), p);
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
                BLOCK_MK
        );
    }

    @Override
    public List<Map<String, String>> getParams() {

        Map<String, String> blockParams = new HashMap<>();

        for (Material block : getBlocks()) {
            blockParams.put(block.name(), getPermission());
        }

        return List.of(blockParams);
    }

    @Override
    public Command getParentCommand() {
        return new de.janschuri.lunaticdrops.commands.drops.blockbreak.BlockBreak();
    }

    private List<Material> getBlocks() {
        if (blocks == null) {
            blocks = Arrays.stream(Material.values())
                    .filter(Material::isBlock)
                    .filter((block) -> LunaticDrops.dropExists(TriggerType.BLOCK_BREAK, block.name()))
                    .toList();
        }
        return blocks;
    }
}
