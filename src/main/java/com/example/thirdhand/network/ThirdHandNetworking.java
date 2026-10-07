package com.example.thirdhand.network;

import com.example.thirdhand.capability.ThirdHandCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ThirdHandNetworking {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new net.minecraft.resources.ResourceLocation("thirdhand", "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, SwapMessage.class, SwapMessage::encode, SwapMessage::decode, SwapMessage::handle);
    }

    /** Сообщение: игрок нажал клавишу — поменять предмет между основной рукой и третьей рукой */
    public static class SwapMessage {
        public SwapMessage() {}

        public static void encode(SwapMessage msg, FriendlyByteBuf buf) {}

        public static SwapMessage decode(FriendlyByteBuf buf) { return new SwapMessage(); }

        public static void handle(SwapMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null) return;
                ItemStack held = player.getMainHandItem();
                player.getCapability(ThirdHandCapability.THIRD_HAND).ifPresent(data -> {
                    ItemStack third = data.getStack();
                    if (held.isEmpty() && !third.isEmpty()) {
                        // забрать предмет из третьей руки
                        player.setItemInHand(InteractionHand.MAIN_HAND, third);
                        data.setStack(ItemStack.EMPTY);
                    } else if (!held.isEmpty() && third.isEmpty()
                            && com.example.thirdhand.util.AndesiteUtil.isAndesite(held)) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        data.setStack(held.copy());
                    }
                    // если в третьей руке уже что-то есть — не даём положить второе
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
