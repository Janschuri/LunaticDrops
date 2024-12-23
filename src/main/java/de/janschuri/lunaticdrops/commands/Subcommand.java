package de.janschuri.lunaticdrops.commands;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticlib.LunaticLanguageConfig;
import de.janschuri.lunaticlib.common.command.AbstractLunaticCommand;

public abstract class Subcommand extends AbstractLunaticCommand {

    @Override
    public LunaticLanguageConfig getLanguageConfig() {
        return LunaticDrops.getLanguageConfig();
    }
}
