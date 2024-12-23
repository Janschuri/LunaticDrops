package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.TriggerType;

import java.util.List;

public enum LootFlag {
    ERASE_VANILLA_DROPS,
    DROP_WITH_SILK_TOUCH,
    DROP_ONLY_TO_PLAYER,
    APPLY_FORTUNE,
    APPLY_LOOTING;

    public static List<LootFlag> getFlags(TriggerType triggerType) {
        List<LootFlag> flags = List.of();
        switch (triggerType) {
            case BLOCK_BREAK:
                flags = List.of(
                        ERASE_VANILLA_DROPS,
                        DROP_WITH_SILK_TOUCH,
                        APPLY_FORTUNE
                );
                break;
            case MOB_KILL:
                flags = List.of(
                        ERASE_VANILLA_DROPS,
                        DROP_ONLY_TO_PLAYER,
                        APPLY_LOOTING
                );
                break;
            case PANDA_EAT:
                flags = List.of();
                break;
        }
        return flags;
    }
}
