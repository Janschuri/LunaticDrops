package de.janschuri.lunaticdrops.commands.drops.harvest;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.gui.BlockBreakEditorGUI;
import de.janschuri.lunaticdrops.gui.HarvestEditorGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.command.HasParams;
import de.janschuri.lunaticlib.common.command.HasParentCommand;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HarvestCreate extends Subcommand implements HasParentCommand, HasParams {

    private static final HarvestCreate INSTANCE = new HarvestCreate();
    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", INSTANCE.getDefaultHelpMessage("Create a harvest drop."))
            .defaultMessage("de", INSTANCE.getDefaultHelpMessage("Erstelle einen Ernte-Drop."));

    private static List<Material> blocks;

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

        if (!getBlocks().contains(block)) {
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
        return new Harvest();
    }

    private List<Material> getBlocks() {
        if (blocks == null) {
            blocks = Arrays.asList(
                    Material.SWEET_BERRY_BUSH,
                    Material.CAVE_VINES_PLANT
            ).stream().filter(
                    material -> !LunaticDrops.dropExists(TriggerType.BLOCK_BREAK, material.name())
            ).toList();
        }

        return blocks;
    }
}
