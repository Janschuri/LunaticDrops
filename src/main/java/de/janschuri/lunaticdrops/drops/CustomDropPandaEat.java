package de.janschuri.lunaticdrops.drops;

import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.inventory.ItemStack;

public class CustomDropPandaEat extends CustomDrop {

    private final ItemStack eatenItem;
    private final boolean matchNBT;

    public CustomDropPandaEat(ItemStack drop, float chance, boolean active, ItemStack eatenItem, boolean matchNBT) {
        super(drop, chance, active);
        this.eatenItem = eatenItem;
        this.matchNBT = matchNBT;
    }

    public boolean matchEatenItem(ItemStack item) {
//        return true;
        if (matchNBT) {
            Logger.debugLog("Matching NBT");
            return item.isSimilar(eatenItem);
        } else {
            Logger.debugLog("Not matching NBT");
            return item.getType() == eatenItem.getType();
        }
    }
}
