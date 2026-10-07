package com.example.thirdhand.client;

import com.example.thirdhand.capability.ThirdHandCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class ThirdHandOverlay {
    public static void register(IEventBus modBus) {
        modBus.addListener(ThirdHandKeybinds::registerKeys);
        modBus.addListener((RegisterGuiOverlaysEvent event) ->
                event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "third_hand", OVERLAY));
        ThirdHandKeybinds.init();
    }

    private static final IGuiOverlay OVERLAY = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        var mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null || mc.options.hideGui) return;

        player.getCapability(ThirdHandCapability.THIRD_HAND).ifPresent(data -> {
            ItemStack stack = data.getStack();
            if (stack.isEmpty()) return;

            // Позиция над хотбаром, аналогично левой руке, но сверху экрана
            int x = screenWidth / 2 + 91 - 8;   // правый край как у offhand
            int y = screenHeight / 2 - 91 - 22; // сверху экрана (над хотбаром)

            guiGraphics.pose().pushPose();
            guiGraphics.renderItem(stack, x, y);
            guiGraphics.renderItemDecorations(mc.font, stack, x, y);
            guiGraphics.pose().popPose();
        });
    };
}
