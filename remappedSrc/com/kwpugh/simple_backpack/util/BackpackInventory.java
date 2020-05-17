package com.kwpugh.simple_backpack.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;

public class BackpackInventory implements Inventory, BackpackInventoryImpl
{
    public DefaultedList<ItemStack> items;
    public int inventory_width;
    public int inventory_height;

    private final Hand hand;
    private final PlayerEntity player;

    public BackpackInventory(CompoundTag items_tag, Hand hand, PlayerEntity player)
    {
        this.hand = hand;
        this.player = player;

        this.fromTag(items_tag);
    }

    @Override
    public void clear()
    {
        this.items.clear();
    }

    @Override
    public int getInventoryWidth()
    {
        return inventory_width;
    }

    @Override
    public int getInventoryHeight()
    {
        return inventory_height;
    }

    @Override
    public int size()
    {
        return getInventoryWidth() * getInventoryHeight();
    }

    @Override
    public boolean isEmpty()
    {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot)
    {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount)
    {
        return Inventories.splitStack(this.items, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot)
    {
        return Inventories.removeStack(this.items, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack)
    {
        this.items.set(slot, stack);
    }

    @Override
    public void markDirty()
    {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player)
    {
        return true;
    }

    public void fromTag(CompoundTag tag)
    {
        this.inventory_width = tag.contains("inventory_width") ? tag.getInt("inventory_width") : 9;
        this.inventory_height = tag.contains("inventory_height") ? tag.getInt("inventory_height") : 6;

        this.items = DefaultedList.ofSize(inventory_width * inventory_height, ItemStack.EMPTY);
        readItemsFromTag(this.items, tag);
    }

    public CompoundTag toTag()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("inventory_width", inventory_width);
        tag.putInt("inventory_height", inventory_height);

        writeItemsToTag(this.items, tag);

        return tag;
    }

    @Override
    public void onOpen(PlayerEntity player)
    {
        Inventory.super.onOpen(player);
        player.playSound(SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 1f, 1f);
    }

    @Override
    public void onClose(PlayerEntity player)
    {
        Inventory.super.onClose(player);

        if(!player.getStackInHand(hand).hasTag())
        {
            player.getStackInHand(hand).setTag(new CompoundTag());
        }

        player.getStackInHand(hand).getTag().put("Backpack", toTag());
        player.playSound(SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 1f, 1f);
    }
}