package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.Drop;
import de.janschuri.lunaticdrops.drops.DropPandaEat;
import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;

public class DropConfigPandaEat extends DropConfig {

    public DropConfigPandaEat(Path path) {
        super(path);
    }

    @Override
    public DropPandaEat getDrop() {
        try {
            Drop drop = super.getDrop();
            String name = getFileName();
            ItemStack eatenItem = getItemStack("eatenItem");
            boolean matchNBT = getBoolean("matchNBT");

            return new DropPandaEat(
                    drop,
                    name,
                    eatenItem,
                    matchNBT
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
