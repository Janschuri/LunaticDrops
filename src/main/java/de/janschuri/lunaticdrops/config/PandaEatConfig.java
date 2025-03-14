package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.PandaEat;
import de.janschuri.lunaticdrops.loot.Loot;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PandaEatConfig extends AbstractDropConfig {

    public PandaEatConfig(Path path) {
        super(path);
    }

    @Override
    public PandaEat getDrop() {
        try {
            String name = getFileName();
            ItemStack eatenItem = getItemStack("eatenItem");
            boolean active = getBoolean("active");
            boolean matchNBT = getBoolean("matchNBT");
            List<Loot> lootList = getLoot("loot");

            if (lootList == null) {
                lootList = new ArrayList<>();
            }

            return new PandaEat(
                    name,
                    lootList,
                    active,
                    eatenItem,
                    matchNBT
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
