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
        return triggerType.getFlags();
    }
}
