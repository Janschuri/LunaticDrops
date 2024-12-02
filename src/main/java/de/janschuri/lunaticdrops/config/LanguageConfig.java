package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticlib.common.config.LunaticLanguageConfigImpl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LanguageConfig extends LunaticLanguageConfigImpl {
    public LanguageConfig(Path dataDirectory, String languageKey) {
        super(dataDirectory, languageKey);
    }

    public List<String> getAliases(String command) {
        return List.of(command);
    }

    public List<String> getAliases(String command, String subcommand) {
        return List.of(subcommand);
    }
}
