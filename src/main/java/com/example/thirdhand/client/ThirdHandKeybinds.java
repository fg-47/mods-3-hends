package com.example.thirdhand.client;

import com.example.thirdhand.network.ThirdHandNetworking;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class ThirdHandKeybinds {
    public static final KeyMapping SWAP_KEY = new KeyMapping(
            "key.thirdhand.swap",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.categories.thirdhand"
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(SWAP_KEY);
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ThirdHandKeybinds::onClientTick);
    }

    private static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && SWAP_KEY.consumeClick()) {
            ThirdHandNetworking.CHANNEL.sendToServer(new ThirdHandNetworking.SwapMessage());
        }
    }
}
