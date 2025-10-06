package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticlib.platform.paper.inventorygui.buttons.InventoryButton;
import de.janschuri.lunaticlib.platform.paper.inventorygui.guis.InventoryGUI;
import de.janschuri.lunaticlib.platform.paper.inventorygui.interfaces.Reopenable;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SelectHarvestGUI extends InventoryGUI implements Reopenable {

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

    @Override
    public NamespacedKey uniqueKey() {
        return MainGUI.UNIQUE_KEY;
    }
}
