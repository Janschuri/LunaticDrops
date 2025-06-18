package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.Utils;
import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import jdk.jshell.execution.Util;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Loot {

    private boolean active = false;
    private String chanceEquation = "0.0";
    private double chance = 0.0;
    private List<LootFlag> flags;
    private List<String> commandsToRun = new ArrayList<>();

    protected Loot() {}

    protected Loot(Loot loot) {
        this.active = loot.active;
        this.chanceEquation = loot.chanceEquation;
        this.chance = loot.chance;
        this.flags = new ArrayList<>(loot.flags);
        this.commandsToRun = new ArrayList<>(loot.commandsToRun);
    }

    protected Loot(double chance, boolean active, List<LootFlag> flags) {
        this.chance = chance;
        this.active = active;
        this.chanceEquation = String.valueOf(chance);

        this.flags = flags != null ? new ArrayList<>(flags) : new ArrayList<>();
    }

    protected Loot(String chanceEquatione, boolean active, List<LootFlag> flags) {
        Double parsedChance = Utils.parseEquation(chanceEquatione);

        if (parsedChance != null) {
            this.chance = parsedChance;
            this.chanceEquation = chanceEquatione;
        }

        this.active = active;
        this.flags = flags != null ? new ArrayList<>(flags) : new ArrayList<>();
    }

    public final List<ItemStack> getDrops() {
        return getDrops(0, List.of());
    }

    public final List<ItemStack> getDrops(List<LootFlag> flags) {
        return getDrops(0, flags);
    }

    public final List<ItemStack> getDrops(int bonusRolls) {
        return getDrops(bonusRolls, List.of());
    }

    public List<ItemStack> getDrops(int bonusRolls, List<LootFlag> flags) {
        return List.of();
    }

    public boolean isActive() {
        return active;
    }

    public double getChance() {
        return chance;
    }

    public String getChanceEquation() {
        return chanceEquation;
    }

    public List<String> getCommandsToRun() {
        return commandsToRun;
    }

    public void runCommands() {
        for (String command : commandsToRun) {
            BukkitLunaticLib.sendConsoleCommand(command);
        }
    }

    public List<LootFlag> getFlags() {
        return flags;
    }

    public boolean hasFlag(LootFlag flag) {
        return flags != null && flags.contains(flag);
    }

    public boolean isEraseVanillaDrops() {
        return hasFlag(LootFlag.ERASE_VANILLA_DROPS);
    }

    public Map<String, Object> toMap() {
        List<String> flagStrings = new ArrayList<>();
        for (LootFlag flag : flags) {
            flagStrings.add(flag.name());
        }

        return Map.of(
                "active", active,
                "chance", chanceEquation,
                "flags", flagStrings,
                "run_commands", commandsToRun
        );
    }

    protected Loot fromMap(Map<String, Object> map) {
        try {
            this.active = Boolean.parseBoolean(map.get("active").toString());
            this.chanceEquation = (String) map.getOrDefault("chance", "0.0");
            this.chance = Utils.parseEquation((String) map.get("chance"));

            List<String> flagStrings = (List<String>) map.getOrDefault("flags", new ArrayList<>());
            this.flags = new ArrayList<>();
            for (String flag : flagStrings) {
                try {
                    flags.add(LootFlag.valueOf(flag));
                } catch (IllegalArgumentException e) {
                    // Ignore unknown flags
                }
            }

            this.commandsToRun = (List<String>) map.getOrDefault("run_commands", new ArrayList<>());

            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ItemStack getDisplayItem() {
        return new ItemStack(org.bukkit.Material.CHEST);
    }
}
