package de.janschuri.lunaticdrops.commands.drops.pandaeat;

import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.commands.drops.LunaticDrops;
import de.janschuri.lunaticdrops.gui.ListDropGUI;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.HasHelpCommand;
import de.janschuri.lunaticlib.commands.HasParentCommand;
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

public class PandaEat extends Subcommand implements HasHelpCommand, HasParentCommand {

    private static final PandaEat INSTANCE = new PandaEat();
    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", INSTANCE.getDefaultHelpMessage("Show the PandaEat help page."))
            .defaultMessage("de", INSTANCE.getDefaultHelpMessage("Zeige die PandaEat Hilfe Seite."));
    private static final CommandMessageKey HELP_HEADER_MK = new LunaticCommandMessageKey(INSTANCE, "help_header")
            .defaultMessage("en", "PandaEat-Help")
            .defaultMessage("de", "PandaEat-Hilfe");

    @Override
    public String getPermission() {
        return "lunaticdrops.admin.pandaeat";
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return Map.of(
                HELP_MK, getPermission()
        );
    }

    @Override
    public String getName() {
        return "panda_eat";
    }

    @Override
    public List<Command> getSubcommands() {
        return List.of(
                new PandaEatCreate(),
                new PandaEatEdit(),
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

        GUIManager.openGUI(new ListDropGUI(TriggerType.PANDA_EAT), p);
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
    public Command getParentCommand() {
        return new LunaticDrops();
    }

    @Override
    public Component getHelpFooter(int i, int i1) {
        return getLanguageConfig().getHelpFooter(this, i, i1);
    }
}
