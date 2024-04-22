package com.kwpugh.simple_backpack.mixin;

import com.kwpugh.simple_backpack.backpack.BackpackItem;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin
{
    @Inject(at = @At(value = "HEAD"), method = "canInsert", cancellable = true)
    public void canInsert(int slot, ItemStack stack, Direction dir, CallbackInfoReturnable<Boolean> cir)
    {
        if(stack.getItem() instanceof BackpackItem)
        {
            cir.setReturnValue(false);
        }
    }
}
