package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootTableEditor extends InventoryGUI {

    private static final Map<Integer, Boolean> active = new HashMap<>();

    public LootTableEditor() {
        super();

        Zombie zombie = new Zombie(null);

        zombie.getLootTable().populateLoot(null, null);
    }



    private InventoryButton chanceButton() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + formatChance(getChance()));
        List<String> lore = List.of("Chance for the drop to happen");
        meta.setLore(lore);
        item.setItemMeta(meta);


        return new InventoryButton()
                .creator((player) -> item);
    }

    private InventoryButton increaseChanceButton() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + formatChance(getChance()));
        List<String> lore = List.of("Chance for the drop to happen");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (processingClickEvent()) {
                        return;
                    }

                    ClickType clickType = event.getClick();

                    if (clickType == ClickType.WINDOW_BORDER_LEFT || clickType == ClickType.WINDOW_BORDER_RIGHT) {
                        return;
                    }

                    switch (clickType) {
                        case SHIFT_RIGHT:
                            increaseChance((Player) event.getWhoClicked(), 0.0001f);
                            break;
                        case RIGHT:
                            increaseChance((Player) event.getWhoClicked(), 0.001f);
                            break;
                        case LEFT:
                            increaseChance((Player) event.getWhoClicked(), 0.01f);
                            break;
                        case SHIFT_LEFT:
                            increaseChance((Player) event.getWhoClicked(), 0.1f);
                            break;
                    }
                });
    }

    private InventoryButton decreaseChanceButton() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Chance: " + formatChance(getChance()));
        List<String> lore = List.of("Chance for the drop to happen");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    if (processingClickEvent()) {
                        return;
                    }

                    ClickType clickType = event.getClick();

                    if (clickType == ClickType.WINDOW_BORDER_LEFT || clickType == ClickType.WINDOW_BORDER_RIGHT) {
                        return;
                    }

                    switch (clickType) {
                        case SHIFT_RIGHT:
                            decreaseChance((Player) event.getWhoClicked(), 0.0001f);
                            break;
                        case RIGHT:
                            decreaseChance((Player) event.getWhoClicked(), 0.001f);
                            break;
                        case LEFT:
                            decreaseChance((Player) event.getWhoClicked(), 0.01f);
                            break;
                        case SHIFT_LEFT:
                            decreaseChance((Player) event.getWhoClicked(), 0.1f);
                            break;
                    }
                });
    }

    private void decreaseChance(Player player, float amount) {
        float newChance = getChance() - amount;

        if (newChance < 0) {
            newChance = 0;
        }

        chances.put(getId(), newChance);

        reloadGui(player);
    }

    private static String formatChance(float chance) {
        return String.format("%.2f", chance * 100) + "%";
    }

    private void increaseChance(Player player, float amount) {
        float newChance = getChance() + amount;

        if (newChance > 1) {
            newChance = 1;
        }

        chances.put(getId(), newChance);

        reloadGui(player);
    }
}
