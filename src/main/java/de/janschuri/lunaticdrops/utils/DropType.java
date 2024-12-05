package de.janschuri.lunaticdrops.utils;

import de.janschuri.lunaticdrops.config.AbstractDropConfig;
import de.janschuri.lunaticdrops.config.BlockBreakConfig;
import de.janschuri.lunaticdrops.config.MobKillConfig;
import de.janschuri.lunaticdrops.config.PandaEatConfig;
import de.janschuri.lunaticdrops.drops.BlockBreak;
import de.janschuri.lunaticdrops.drops.CustomDrop;
import de.janschuri.lunaticdrops.drops.MobKill;
import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.gui.BlockBreakEditorGUI;
import de.janschuri.lunaticdrops.gui.EditorGUI;
import de.janschuri.lunaticdrops.gui.MobKillEditorGUI;
import de.janschuri.lunaticdrops.gui.PandaEatEditorGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.nio.file.LinkOption;
import java.nio.file.Path;

public enum DropType {
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
        public EditorGUI getEditorGUI(Player player, CustomDrop drop) {
            if (drop instanceof PandaEat) {
                return new PandaEatEditorGUI(player, (PandaEat) drop);
            }
            Logger.errorLog("Drop is not an instance of PandaEat");
            return null;
        }
        @Override
        public EditorGUI getEditorGUI(Player player, String name) {
            return new PandaEatEditorGUI(player, name);
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
        public MobKillEditorGUI getEditorGUI(Player player, CustomDrop drop) {
            if (drop instanceof MobKill) {
                return new MobKillEditorGUI(player, (MobKill) drop);
            }
            Logger.errorLog("Drop is not an instance of MobKill");
            return null;
        }
        @Override
        public MobKillEditorGUI getEditorGUI(Player player, String name) {
            return new MobKillEditorGUI(player, name);
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
        public BlockBreakEditorGUI getEditorGUI(Player player, CustomDrop drop) {
            if (drop instanceof BlockBreak) {
                return new BlockBreakEditorGUI(player, (BlockBreak) drop);
            }
            Logger.errorLog("Drop is not an instance of BlockBreak");
            return null;
        }
        @Override
        public BlockBreakEditorGUI getEditorGUI(Player player, String name) {
            return new BlockBreakEditorGUI(player, name);
        }
    };

    public abstract String getDisplayName();
    public abstract ItemStack getDisplayItem();
    public abstract String getConfigPath();
    public abstract AbstractDropConfig getConfig(Path path);
    public abstract EditorGUI getEditorGUI(Player player, CustomDrop drop);
    public abstract EditorGUI getEditorGUI(Player player, String name);

    public static DropType fromString(String string) {
        for (DropType dropType : DropType.values()) {
            if (dropType.name().equalsIgnoreCase(string)) {
                return dropType;
            }
        }
        return null;
    }
}
