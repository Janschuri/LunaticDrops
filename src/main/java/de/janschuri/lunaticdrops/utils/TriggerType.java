package de.janschuri.lunaticdrops.utils;

import de.janschuri.lunaticdrops.config.*;
import de.janschuri.lunaticdrops.drops.*;
import de.janschuri.lunaticdrops.gui.editor.*;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.loot.LootFlag;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;
import java.util.List;

public enum TriggerType {
    PANDA_EAT {
        @Override
        public String getDisplayName() {
            return "Panda Eat";
        }
        @Override
        public ItemStack getDisplayItem() {
            return new ItemStack(Material.BAMBOO);
        }
        @Override
        public String getConfigPath() {
            return "/panda_eat";
        }
        @Override
        public DropConfigPandaEat getConfig(Path path) {
            return new DropConfigPandaEat(path);
        }
        @Override
        public EditorGUI getEditorGUI(Drop drop) {
            if (drop instanceof DropPandaEat) {
                return new EditorGUIPandaEat((DropPandaEat) drop);
            }
            Logger.errorLog("Drop is not an instance of PandaEat");
            return null;
        }
        @Override
        public EditorGUI getEditorGUI() {
            return new EditorGUIPandaEat();
        }
        @Override
        public List<LootFlag> getFlags() {
            return List.of(LootFlag.FORCE_MAX_AMOUNT);
        }
    },
    MOB_KILL {
        @Override
        public String getDisplayName() {
            return "Mob Kill";
        }
        @Override
        public ItemStack getDisplayItem() {
            return new ItemStack(Material.DIAMOND_SWORD);
        }
        @Override
        public String getConfigPath() {
            return "/mob_kill";
        }
        @Override
        public DropConfigMobKill getConfig(Path path) {
            return new DropConfigMobKill(path);
        }
        @Override
        public EditorGUIMobKill getEditorGUI(Drop drop) {
            if (drop instanceof DropMobKill) {
                return new EditorGUIMobKill((DropMobKill) drop);
            }
            Logger.errorLog("Drop is not an instance of MobKill");
            return null;
        }
        @Override
        public EditorGUIMobKill getEditorGUI() {
            return new EditorGUIMobKill();
        }
        @Override
        public List<LootFlag> getFlags() {
            return List.of(LootFlag.ERASE_VANILLA_DROPS, LootFlag.DROP_ONLY_TO_PLAYER, LootFlag.APPLY_LOOTING, LootFlag.FORCE_MAX_AMOUNT);
        }
    },
    HARVEST {
        @Override
        public String getDisplayName() {
            return "Harvest";
        }
        @Override
        public ItemStack getDisplayItem() {
            return new ItemStack(Material.SWEET_BERRIES);
        }
        @Override
        public String getConfigPath() {
            return "/harvest";
        }
        @Override
        public DropConfigHarvest getConfig(Path path) {
            return new DropConfigHarvest(path);
        }
        @Override
        public EditorGUIHarvest getEditorGUI(Drop drop) {
            if (drop instanceof DropHarvest) {
                return new EditorGUIHarvest((DropHarvest) drop);
            }
            Logger.errorLog("Drop is not an instance of Harvest");
            return null;
        }
        @Override
        public EditorGUIHarvest getEditorGUI() {
            return new EditorGUIHarvest();
        }
        @Override
        public List<LootFlag> getFlags() {
            return List.of(LootFlag.ERASE_VANILLA_DROPS, LootFlag.FORCE_MAX_AMOUNT);
        }
    },
    BLOCK_BREAK {
        @Override
        public String getDisplayName() {
            return "Block Break";
        }
        @Override
        public ItemStack getDisplayItem() {
            return new ItemStack(Material.DIAMOND_PICKAXE);
        }
        @Override
        public String getConfigPath() {
            return "/block_break";
        }
        @Override
        public DropConfigBlockBreak getConfig(Path path) {
            return new DropConfigBlockBreak(path);
        }
        @Override
        public EditorGUIBlockBreak getEditorGUI(Drop drop) {
            if (drop instanceof DropBlockBreak) {
                return new EditorGUIBlockBreak((DropBlockBreak) drop);
            }
            Logger.errorLog("Drop is not an instance of BlockBreak");
            return null;
        }
        @Override
        public EditorGUIBlockBreak getEditorGUI() {
            return new EditorGUIBlockBreak();
        }
        @Override
        public List<LootFlag> getFlags() {
            return List.of(
                    LootFlag.ERASE_VANILLA_DROPS,
                    LootFlag.DROP_WITH_SILK_TOUCH,
                    LootFlag.APPLY_FORTUNE,
                    LootFlag.FORCE_MAX_AMOUNT,
                    LootFlag.ONLY_FULL_GROWN
            );
        }
    },
    LEAVES_DECAY {
        @Override
        public String getDisplayName() {
            return "Leaves Decay";
        }
        @Override
        public ItemStack getDisplayItem() {
            return new ItemStack(Material.OAK_LEAVES);
        }
        @Override
        public String getConfigPath() {
            return "/leaves_decay";
        }
        @Override
        public DropConfigBlockDecay getConfig(Path path) {
            return new DropConfigBlockDecay(path);
        }
        @Override
        public EditorGUILeavesDecay getEditorGUI(Drop drop) {
            if (drop instanceof DropLeavesDecay) {
                return new EditorGUILeavesDecay((DropLeavesDecay) drop);
            }
            Logger.errorLog("Drop is not an instance of LeavesDecay");
            return null;
        }
        @Override
        public EditorGUILeavesDecay getEditorGUI() {
            return new EditorGUILeavesDecay();
        }
        @Override
        public List<LootFlag> getFlags() {
            return List.of(LootFlag.ERASE_VANILLA_DROPS, LootFlag.FORCE_MAX_AMOUNT);
        }
    },
    ENTITY_BREED {
        @Override
        public String getDisplayName() {
            return "Entity Breed";
        }
        @Override
        public ItemStack getDisplayItem() {
            return new ItemStack(Material.LEAD);
        }
        @Override
        public String getConfigPath() {
            return "/entity_breed";
        }
        @Override
        public DropConfigEntityBreed getConfig(Path path) {
            return new DropConfigEntityBreed(path);
        }
        @Override
        public EditorGUIEntityBreed getEditorGUI(Drop drop) {
            if (drop instanceof DropEntityBreed) {
                return new EditorGUIEntityBreed((DropEntityBreed) drop);
            }
            Logger.errorLog("Drop is not an instance of LeavesDecay");
            return null;
        }
        @Override
        public EditorGUIEntityBreed getEditorGUI() {
            return new EditorGUIEntityBreed();
        }
        @Override
        public List<LootFlag> getFlags() {
            return List.of(
                    LootFlag.FORCE_MAX_AMOUNT,
                    LootFlag.DROP_ONLY_TO_PLAYER
            );
        }
    };

    public abstract String getDisplayName();
    public abstract ItemStack getDisplayItem();
    public abstract String getConfigPath();
    public abstract DropConfig getConfig(Path path);
    public abstract EditorGUI getEditorGUI(Drop drop);
    public abstract EditorGUI getEditorGUI();
    public abstract List<LootFlag> getFlags();

    public static TriggerType fromString(String string) {
        for (TriggerType dropType : TriggerType.values()) {
            if (dropType.name().equalsIgnoreCase(string)) {
                return dropType;
            }
        }
        return null;
    }

}
