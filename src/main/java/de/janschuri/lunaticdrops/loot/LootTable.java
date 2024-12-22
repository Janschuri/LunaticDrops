package de.janschuri.lunaticdrops.loot;

import de.janschuri.lunaticdrops.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LootTable implements Loot {

    private final List<Loot> lootList;
    private final float chance;
    private final boolean active;
    private final boolean cumulative;
    private boolean eraseVanillaDrops = false;

    public LootTable(List<Loot> lootList, float chance, boolean active, boolean cumulative) {
        this.lootList = lootList;
        this.chance = chance;
        this.active = active;
        this.cumulative = cumulative;
    }

    @Override
    public List<ItemStack> getDrops() {
        eraseVanillaDrops = false;

        if (cumulative) {
            float[] chances = new float[lootList.size()];

            for (int i = 0; i < lootList.size(); i++) {
                chances[i] = lootList.get(i).getChance();
            }

            int luckyIndex = Utils.isLucky(chances);

            if (luckyIndex == -1) {
                return new ArrayList<>();
            }

            return lootList.get(luckyIndex).getDrops();
        }


        List<ItemStack> drops = new ArrayList<>();

        for (Loot loot : lootList) {
            if (Utils.isLucky(loot.getChance())) {
                drops.addAll(loot.getDrops());

                if (loot.isEraseVanillaDrops()) {
                    eraseVanillaDrops = true;
                }
            }
        }

        return drops;
    }

    @Override
    public float getChance() {
        return 0;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of();
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.ENDER_CHEST);
    }

    @Override
    public boolean isEraseVanillaDrops() {
        return eraseVanillaDrops;
    }

    public static LootTable fromMap(Map<String, Object> map) {
        List<Map<String, Object>> lootMapList = (List<Map<String, Object>>) map.get("loot");

        List<Loot> lootList = new ArrayList<>();

        for (Map<String, Object> lootMap : lootMapList) {
            lootList.add(SingleLoot.fromMap(lootMap));
        }

        return new LootTable(
                lootList,
                (float) map.get("chance"),
                (boolean) map.get("active"),
                (boolean) map.get("cumulative")
        );
    }
}
