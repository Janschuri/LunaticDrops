package de.janschuri.lunaticdrops.commands.drops;

import de.janschuri.lunaticdrops.commands.Subcommand;
import de.janschuri.lunaticdrops.commands.drops.blockbreak.BlockBreak;
import de.janschuri.lunaticdrops.commands.drops.harvest.Harvest;
import de.janschuri.lunaticdrops.commands.drops.mobkill.MobKill;
import de.janschuri.lunaticdrops.commands.drops.pandaeat.PandaEat;
import de.janschuri.lunaticdrops.gui.MainGUI;
import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.HasHelpCommand;
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

public class LunaticDrops extends Subcommand implements HasSubcommands, HasHelpCommand {

    private static final LunaticDrops INSTANCE = new LunaticDrops();

    private static final CommandMessageKey HELP_MK = new LunaticCommandMessageKey(INSTANCE, "help")
            .defaultMessage("en", "&6/%command% %subcommand% &7- Show the LunaticDrops help page.")
            .defaultMessage("de", "&6/%command% %subcommand% &7- Zeige die LunaticDrops Hilfe Seite.");
    private static final CommandMessageKey HELP_HEADER_MK = new LunaticCommandMessageKey(INSTANCE, "help_header")
            .defaultMessage("en", "LunaticDrops-Help")
            .defaultMessage("de", "LunaticDrops-Hilfe");

    @Override
    public String getPermission() {
        return "lunaticdrops.admin";
    }

    @Override
    public String getName() {
        return "lunaticdrops";
    }

    @Override
    public List<Command> getSubcommands() {
        return List.of(
                new LunaticDropsReload(),
                new BlockBreak(),
                new MobKill(),
                new PandaEat(),
                new Harvest(),
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

        GUIManager.reopenGUI(new MainGUI(), p);
        return true;
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return Map.of(
                HELP_MK, getPermission()
        );
    }

    @Override
    public boolean isPrimaryCommand() {
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
}
