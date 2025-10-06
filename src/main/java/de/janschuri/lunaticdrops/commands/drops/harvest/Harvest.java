package de.janschuri.lunaticdrops.commands.drops.harvest;

import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.commands.drops.LunaticDrops;
import de.janschuri.lunaticdrops.gui.ListDropGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.HasHelpCommand;
import de.janschuri.lunaticlib.commands.HasParentCommand;
import de.janschuri.lunaticlib.commands.HasSubcommands;
import de.janschuri.lunaticlib.config.CommandMessageKey;
import de.janschuri.lunaticlib.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.platform.paper.inventorygui.handler.GUIManager;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class Harvest extends Subcommand implements HasSubcommands, HasHelpCommand, HasParentCommand {

    private static final Harvest INSTANCE = new Harvest();
    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", INSTANCE.getDefaultHelpMessage("Show the Harvest help page."))
            .defaultMessage("de", "&6/%command% %subcommand% &7- Zeige die Harvest Hilfe Seite.");
    private static final CommandMessageKey HELP_HEADER_MK = new LunaticCommandMessageKey(INSTANCE, "help_header")
            .defaultMessage("en", "Harvest-Help")
            .defaultMessage("de", "Harvest-Hilfe");

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.harvest";
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return Map.of(
                HELP_MK, getPermission()
        );
    }

    @Override
    public String getName() {
        return "harvest";
    }

    @Override
    public List<Command> getSubcommands() {
        return List.of(
                new HarvestCreate(),
                new HarvestEdit(),
                getHelpCommand()
        );
    }

    @Override
    public boolean handleNoMatchingSubcommand(Sender sender, String[] args) {
        if (!(sender instanceof PlayerSender player)) {
            sender.sendMessage(getMessage(NO_CONSOLE_COMMAND_MK));
            return true;
        }

        Player p = Bukkit.getPlayer(player.getUniqueId());

        GUIManager.openGUI(new ListDropGUI(TriggerType.HARVEST), p);
        return true;
    }

    @Override
    public Component getPageParam() {
        return getMessage(PAGE_MK.noPrefix());
    }

    @Override
    public Component getHelpHeader() {
        return getMessage(HELP_HEADER_MK.noPrefix());
    }

    @Override
    public Component getHelpFooter(int i, int i1) {
        return getLanguageConfig().getHelpFooter(this, i, i1);
    }

    @Override
    public Command getParentCommand() {
        return new LunaticDrops();
    }
}
