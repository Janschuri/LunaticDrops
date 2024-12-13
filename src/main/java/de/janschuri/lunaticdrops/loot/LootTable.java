package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.Logger;
import de.janschuri.lunaticdrops.utils.Utils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LootTable implements Loot {

    private List<Loot> lootList;
    private float chance;
    private boolean cumulative;

    public LootTable(List<Loot> lootList, float chance, boolean cumulative) {
        super();
        this.lootList = lootList;
        this.chance = chance;
        this.cumulative = cumulative;
    }

    @Override
    public List<ItemStack> getDrops() {
        if (cumulative) {
            return getCumulativeDrops();
        } else {
            return getIndependentDrops();
        }
    }

    private List<ItemStack> getCumulativeDrops() {
        float[] chances = new float[lootList.size()];

        for (int i = 0; i < lootList.size(); i++) {
            chances[i] = lootList.get(i).getChance();
        }

        int index = Utils.isLucky(chances);

        if (index == -1) {
            return List.of();
        }

        return lootList.get(index).getDrops();
    }

    private List<ItemStack> getIndependentDrops() {
        List<ItemStack> drops = new ArrayList<>();

        for (Loot loot : lootList) {
            if (Utils.isLucky(loot.getChance())) {
                drops.addAll(loot.getDrops());
            }
        }

        return drops;
    }

    @Override
    public float getChance() {
        return chance;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of(
            "lootList", lootList,
            "chance", chance,
            "cumulative", cumulative
        );
    }

    public static LootTable fromMap(Map<String, Object> map) {
        List<Object> lootList;
        float chance;
        boolean cumulative;

        if (map.containsKey("lootList")) {
            if (map.get("lootList") instanceof List) {
                lootList = (List<Object>) map.get("lootList");
            } else {
                Logger.errorLog("LootTable.fromMap: lootList is not a List");
                return null;
            }
        } else {
            Logger.errorLog("LootTable.fromMap: lootList not found");
            return null;
        }

        if (map.containsKey("chance")) {
            if (map.get("chance") instanceof Float) {
                chance = (float) map.get("chance");
            } else {
                Logger.errorLog("LootTable.fromMap: chance is not a Float");
                return null;
            }
        } else {
            Logger.errorLog("LootTable.fromMap: chance not found");
            return null;
        }

        if (map.containsKey("cumulative")) {
            if (map.get("cumulative") instanceof Boolean) {
                cumulative = (boolean) map.get("cumulative");
            } else {
                Logger.errorLog("LootTable.fromMap: cumulative is not a Boolean");
                return null;
            }
        } else {
            Logger.errorLog("LootTable.fromMap: cumulative not found");
            return null;
        }

        List<Loot> lootListObjects = new ArrayList<>();

        for (Object loot : lootList) {
            if (loot instanceof Map) {
                Map<String, Object> lootMap = (Map<String, Object>) loot;
                String type = (String) lootMap.get("type");

                if (type == null) {
                    Logger.errorLog("LootTable.fromMap: No type found in loot");
                    return null;
                }

                switch (type) {
                    case "single":
                        SingleLoot singleLoot = SingleLoot.fromMap(lootMap);
                        if (singleLoot == null) {
                            Logger.errorLog("LootTable.fromMap: SingleLoot is null");
                            return null;
                        }
                        break;
                    case "table":
                        LootTable lootTable = LootTable.fromMap(lootMap);
                        if (lootTable == null) {
                            Logger.errorLog("LootTable.fromMap: LootTable is null");
                            return null;
                        }
                        break;
                    default:
                        Logger.errorLog("LootTable.fromMap: Unknown loot type: " + type);
                        return null;
                }
            } else {
                Logger.errorLog("LootTable.fromMap: Loot is not a Map");
                return null;
            }
        }

        return new LootTable(lootListObjects, chance, cumulative);
    }
}
