package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.SelectBlockGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.PaginatedList;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.SearchableList;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SelectHarvestGUI extends InventoryGUI {

    private Consumer<Material> consumer;
    private String search = "";
    private Integer page = 0;

    public SelectHarvestGUI() {
        super();
    }

    public SelectHarvestGUI consumer(Consumer<Material> consumer) {
        this.consumer = consumer;
        return this;
    }

    public Consumer<Material> getConsumer() {
        return consumer;
    }

    @Override
    public void init(Player player) {
        for (int i = 0; i < 9; i++) {
            addButton(i, emptyButton(i));
        }

        addButton(9, glowBerrieButton());
        addButton(10, sweetBerrieButton());


        for (int i = 11; i < 45; i++) {
            addButton(i, emptyListItemButton());
        }

        for (int i = 45; i < 54; i++) {
            addButton(i, emptyButton(i));
        }

        super.init(player);
    }

    public InventoryButton glowBerrieButton() {
        ItemStack itemStack = new ItemStack(Material.GLOW_BERRIES);


        return new InventoryButton()
                .creator(player -> itemStack)
                .consumer(event -> {
                    getConsumer().accept(Material.CAVE_VINES_PLANT);
                });
    }

    public InventoryButton sweetBerrieButton() {
        ItemStack itemStack = new ItemStack(Material.SWEET_BERRIES);


        return new InventoryButton()
                .creator(player -> itemStack)
                .consumer(event -> {
                    getConsumer().accept(Material.SWEET_BERRY_BUSH);
                });
    }

    public InventoryButton emptyListItemButton() {
        return new InventoryButton()
                .creator(player -> new ItemStack(Material.AIR));
    }
}
