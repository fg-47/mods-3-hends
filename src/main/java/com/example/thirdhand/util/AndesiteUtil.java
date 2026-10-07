package com.example.thirdhand.util;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class AndesiteUtil {
    /**
     * Проверяет, что предмет — это андезит или одна из его разновидностей:
     * андезит, полированный андезит, плиты, ступеньки, стены из андезита.
     * Проще всего — по имени блока в реестре.
     */
    public static boolean isAndesite(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof BlockItem blockItem)) return false;
        var key = ForgeRegistries.BLOCKS.getKey(blockItem.getBlock());
        if (key == null) return false;
        return key.getPath().contains("andesite");
    }
}
