package de.janschuri.lunaticdrops.utils;

import de.janschuri.lunaticdrops.config.*;
import de.janschuri.lunaticdrops.drops.*;
import de.janschuri.lunaticdrops.gui.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;

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
        public PandaEatConfig getConfig(Path path) {
            return new PandaEatConfig(path);
        }
        @Override
        public EditorGUI getEditorGUI(CustomDrop drop) {
            if (drop instanceof PandaEat) {
                return new PandaEatEditorGUI((PandaEat) drop);
            }
            Logger.errorLog("Drop is not an instance of PandaEat");
            return null;
        }
        @Override
        public EditorGUI getEditorGUI() {
            return new PandaEatEditorGUI();
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
        public MobKillConfig getConfig(Path path) {
            return new MobKillConfig(path);
        }
        @Override
        public MobKillEditorGUI getEditorGUI(CustomDrop drop) {
            if (drop instanceof MobKill) {
                return new MobKillEditorGUI((MobKill) drop);
            }
            Logger.errorLog("Drop is not an instance of MobKill");
            return null;
        }
        @Override
        public MobKillEditorGUI getEditorGUI() {
            return new MobKillEditorGUI();
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
        public HarvestConfig getConfig(Path path) {
            return new HarvestConfig(path);
        }
        @Override
        public HarvestEditorGUI getEditorGUI(CustomDrop drop) {
            if (drop instanceof Harvest) {
                return new HarvestEditorGUI((Harvest) drop);
            }
            Logger.errorLog("Drop is not an instance of Harvest");
            return null;
        }
        @Override
        public HarvestEditorGUI getEditorGUI() {
            return new HarvestEditorGUI();
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
        public BlockBreakConfig getConfig(Path path) {
            return new BlockBreakConfig(path);
        }
        @Override
        public BlockBreakEditorGUI getEditorGUI(CustomDrop drop) {
            if (drop instanceof BlockBreak) {
                return new BlockBreakEditorGUI((BlockBreak) drop);
            }
            Logger.errorLog("Drop is not an instance of BlockBreak");
            return null;
        }
        @Override
        public BlockBreakEditorGUI getEditorGUI() {
            return new BlockBreakEditorGUI();
        }
    };

    public abstract String getDisplayName();
    public abstract ItemStack getDisplayItem();
    public abstract String getConfigPath();
    public abstract AbstractDropConfig getConfig(Path path);
    public abstract EditorGUI getEditorGUI(CustomDrop drop);
    public abstract EditorGUI getEditorGUI();

    public static TriggerType fromString(String string) {
        for (TriggerType dropType : TriggerType.values()) {
            if (dropType.name().equalsIgnoreCase(string)) {
                return dropType;
            }
        }
        return null;
    }
}
