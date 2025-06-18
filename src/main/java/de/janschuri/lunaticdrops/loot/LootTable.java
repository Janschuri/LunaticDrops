package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LootTable extends Loot {

    private List<Loot> lootList = new ArrayList<>();
    private boolean cumulative = false;
    private List<String> runCommands = new ArrayList<>();
    private boolean eraseVanillaDrops = false;
    private List<String> runCommandsLoot = new ArrayList<>();

    public LootTable() {}

    public LootTable(Loot loot,  List<Loot> lootList, boolean cumulative) {
        super(loot);
        this.lootList = lootList;
        this.cumulative = cumulative;
    }

    public LootTable(float chance, boolean active, List<Loot> lootList, boolean cumulative, LootFlag... flags) {
        super(chance, active, List.of(flags));
        this.lootList = lootList;
        this.cumulative = cumulative;
    }

    public LootTable(String chanceString, boolean active, List<Loot> lootList, boolean cumulative, LootFlag... flags) {
        super(chanceString, active, List.of(flags));
        this.lootList = lootList;
        this.cumulative = cumulative;
    }

    @Override
    public List<ItemStack> getDrops(int bonusRolls, List<LootFlag> flags) {
        eraseVanillaDrops = false;
        runCommands = new ArrayList<>();

        if (cumulative) {
            double[] chances = new double[lootList.size()];

            for (int i = 0; i < lootList.size(); i++) {
                chances[i] = lootList.get(i).getChance();
            }

            int luckyIndex = Utils.isLucky(chances);

            if (luckyIndex == -1) {
                return new ArrayList<>();
            }

            return lootList.get(luckyIndex).getDrops(bonusRolls, flags);
        }


        List<ItemStack> dropResults = new ArrayList<>();

        for (Loot loot : lootList) {
            if (Utils.isLucky(loot.getChance())) {
                dropResults.addAll(loot.getDrops(bonusRolls, flags));

                if (loot.hasFlag(LootFlag.ERASE_VANILLA_DROPS)) {
                    eraseVanillaDrops = true;
                }
            }
        }

        return dropResults;
    }

    @Override
    public boolean isEraseVanillaDrops () {
        return eraseVanillaDrops || hasFlag(LootFlag.ERASE_VANILLA_DROPS);
    }

    @Override
    public List<String> getCommandsToRun() {
        List<String> runCommands = runCommandsLoot;
        runCommands.addAll(super.getCommandsToRun());
        return runCommands;
    }

    @Override
    public Map<String, Object> toMap() {
        //TODO: Implement toMap for LootTable
        throw new UnsupportedOperationException("toMap() is not implemented for LootTable");
    }

    @Override
    public ItemStack getDisplayItem() {
        return new ItemStack(Material.ENDER_CHEST);
    }

    @Override
    public LootTable fromMap(Map<String, Object> map) {
        try {
            List<Map<String, Object>> lootMapList = (List<Map<String, Object>>) map.get("loot");

            List<Loot> lootList = new ArrayList<>();

            for (Map<String, Object> lootMap : lootMapList) {
                lootList.add(new SingleLoot().fromMap(lootMap));
            }

            Loot loot = super.fromMap(map);
            boolean cumulative = (Boolean) map.get("cumulative");


            return new LootTable(
                    loot,
                    lootList,
                    cumulative
            );
        } catch (Exception e) {
            Logger.errorLog("Error loading LootTable: " + e.getMessage());
            return new LootTable();
        }
    }
}
