package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticlib.common.config.LunaticLanguageConfig;

import java.nio.file.Path;

public class LanguageConfig extends LunaticLanguageConfig {
    public LanguageConfig(Path dataDirectory, String languageKey) {
        super(dataDirectory, languageKey);
    }

    @Override
    protected String getPackage() {
        return "de.janschuri.lunaticdrops";
    }
}
