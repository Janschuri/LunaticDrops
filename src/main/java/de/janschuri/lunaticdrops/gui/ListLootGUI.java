package de.janschuri.lunaticdrops.gui;

import de.janschuri.lunaticdrops.LunaticDrops;
import de.janschuri.lunaticdrops.drops.Drop;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.TriggerType;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.Reopenable;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.PaginatedList;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.SearchableList;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static de.janschuri.lunaticdrops.utils.Utils.formatChance;

public class ListLootGUI extends ListGUI<ListLootGUI.ListLootItem> implements PaginatedList<ListLootGUI.ListLootItem>, SearchableList<ListLootGUI.ListLootItem>, Reopenable {

    private int page = 0;
    private String search = "";
    private Consumer<InventoryClickEvent> consumer  = event -> {
        Player player = (Player) event.getWhoClicked();
        GUIManager.openGUI(new MainGUI(), player);
    };

    @Override
    public void init(Player player) {
        addButton(0, returnButton());
        super.init(player);
    }

    @Override
    public InventoryButton listItemButton(ListLootGUI.ListLootItem lootItem) {
        Loot loot = lootItem.getLoot();
        Drop drop = lootItem.getDrop();
        ItemStack item = loot.getDisplayItem();

        String typeString = drop.getTriggerType().getDisplayName();
        String displayName = drop.getName();

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = new ArrayList<>();
            String chanceLore = formatChance(loot.getChance());

            if (loot.getChanceEquation() != String.valueOf(loot.getChance())) {
                chanceLore += " (" + loot.getChanceEquation() + ")";
            }

            lore.add(chanceLore);

            lore.add("Type: " + typeString);
            lore.add("Drop Name: " + displayName);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return new InventoryButton()
                .creator((player) -> item)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    Consumer<InventoryClickEvent> returnConsumer = e -> {
                        GUIManager.openGUI(this, player);
                    };

                    if (drop != null) {
                        GUIManager.openGUI(
                                drop.getTriggerType().getEditorGUI(drop)
                                        .consumer(returnConsumer),
                                player
                        );
                    } else {
                        player.sendMessage("§cThis loot does not belong to a custom drop.");
                    }
                });
    }

    @Override
    public List<ListLootGUI.ListLootItem> getItems() {
        List<ListLootGUI.ListLootItem> loots = new ArrayList<>();

        TriggerType[] triggerTypes = TriggerType.values();

        List<Drop> customDrops = new ArrayList<>();

        for (TriggerType triggerType : triggerTypes) {
            List<Drop> drops = LunaticDrops.getDrops(triggerType);
            customDrops.addAll(drops);
        }

        for (Drop drop : customDrops) {
            List<Loot> dropLoots = drop.getLoot();

            for (Loot loot : dropLoots) {
                ListLootItem listLootItem = new ListLootItem(drop, loot);
                loots.add(listLootItem);
            }
        }

        return loots;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int i) {
        this.page = i;
    }

    @Override
    public Predicate<ListLootGUI.ListLootItem> getSearchFilter(Player player) {
        return lootItem -> {
            ItemMeta meta = lootItem.getLoot().getDisplayItem().getItemMeta();
            if (meta != null) {
                String displayName = meta.getDisplayName();

                if (displayName != null && displayName.toLowerCase().contains(this.search.toLowerCase())) {
                    return true;
                }
            }

            if (lootItem.getLoot().getDisplayItem().getType().toString().toLowerCase().contains(this.search.toLowerCase())) {
                return true;
            }
            return false;
        };
    }

    @Override
    public String getSearch() {
        return search;
    }

    @Override
    public void setSearch(String s) {
        this.search = s.toLowerCase();
    }

    public class ListLootItem {
        private Loot loot;
        private Drop drop;

        private ListLootItem(Drop drop, Loot loot) {
            this.drop = drop;
            this.loot = loot;
        }

        public Loot getLoot() {
            return loot;
        }

        public Drop getDrop() {
            return drop;
        }
    }

    @Override
    public NamespacedKey uniqueKey() {
        return MainGUI.UNIQUE_KEY;
    }

    @Override
    public int getSearchSlot() {
        return 4;
    }

    private InventoryButton returnButton() {
        ItemStack itemStack = new ItemStack(Material.ARROW);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§cReturn");
        itemStack.setItemMeta(meta);

        return new InventoryButton()
                .creator((player) -> itemStack)
                .consumer(consumer);
    }

    public ListLootGUI consumer(Consumer<InventoryClickEvent> consumer) {
        this.consumer = consumer;
        return this;
    }

}
