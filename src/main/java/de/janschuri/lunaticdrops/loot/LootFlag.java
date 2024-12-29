package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.TriggerType;

import java.util.List;

public enum LootFlag {
    ERASE_VANILLA_DROPS {
        @Override
        public String getDisplayName() {
            return "Erase Vanilla Drops";
        }
    },
    DROP_WITH_SILK_TOUCH {
        @Override
        public String getDisplayName() {
            return "Drop with Silk Touch";
        }
    },
    DROP_ONLY_TO_PLAYER {
        @Override
        public String getDisplayName() {
            return "Drop only to Player";
        }
    },
    APPLY_FORTUNE {
        @Override
        public String getDisplayName() {
            return "Apply Fortune";
        }
    },
    APPLY_LOOTING {
        @Override
        public String getDisplayName() {
            return "Apply Looting";
        }
    },
    FORCE_MAX_AMOUNT {
        @Override
        public String getDisplayName() {
            return "Force Max Amount";
        }
    },
    ONLY_FULL_GROWN {
        @Override
        public String getDisplayName() {
            return "Only Full Grown";
        }
    };

    public String getDisplayName() {
        return name();
    }

    public static List<LootFlag> getFlags(TriggerType triggerType) {
        List<LootFlag> flags = List.of();
        switch (triggerType) {
            case BLOCK_BREAK:
                flags = List.of(
                        ERASE_VANILLA_DROPS,
                        DROP_WITH_SILK_TOUCH,
                        APPLY_FORTUNE,
                        FORCE_MAX_AMOUNT,
                        ONLY_FULL_GROWN
                );
                break;
            case MOB_KILL:
                flags = List.of(
                        ERASE_VANILLA_DROPS,
                        DROP_ONLY_TO_PLAYER,
                        APPLY_LOOTING,
                        FORCE_MAX_AMOUNT
                );
                break;
            case PANDA_EAT:
                flags = List.of(
                        FORCE_MAX_AMOUNT
                );
                break;
            case HARVEST:
                flags = List.of(
                        ERASE_VANILLA_DROPS,
                        FORCE_MAX_AMOUNT
                );
                break;
        }
        return flags;
    }
}
