package de.janschuri.lunaticdrops.events;

import org.bukkit.entity.Panda;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PandaEatDropItemEvent extends Event implements Cancellable {

    private final Panda panda;
    private final ItemStack consumedItem;
    private final List<ItemStack> drops = new ArrayList<>();
    private boolean cancelled;

    public PandaEatDropItemEvent(Panda panda, ItemStack consumedItem) {
        super(false);
        this.panda = panda;
        this.consumedItem = consumedItem;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public ItemStack getConsumedItem() {
        return consumedItem;
    }

    public Panda getPanda() {
        return panda;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
