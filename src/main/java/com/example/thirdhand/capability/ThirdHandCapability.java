package com.example.thirdhand.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = "thirdhand")
public class ThirdHandCapability {
    public static final Capability<IThirdHand> THIRD_HAND = CapabilityManager.get(new CapabilityToken<>() {});
    private static final ResourceLocation ID = new ResourceLocation("thirdhand", "third_hand");

    public static void register() {
        // Сама регистрация происходит через @Mod.EventBusSubscriber и AttachCapabilitiesEvent ниже
    }

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<net.minecraft.world.entity.player.Player> event) {
        event.addCapability(ID, new ICapabilitySerializable<CompoundTag>() {
            private final ThirdHandData data = new ThirdHandData();
            private final LazyOptional<IThirdHand> opt = LazyOptional.of(() -> data);

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return THIRD_HAND.orEmpty(cap, opt);
            }

            @Override
            public CompoundTag serializeNBT() {
                return data.serializeNBT();
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                data.deserializeNBT(nbt);
            }
        });
    }

    /** Копируем данные при смерти/возрождении и в другой мир (respawn / dimension change) */
    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath() && !event.getEntity().level().isClientSide) {
            // даже при переходе в другой мер копируем (isWasDeath=false, но isEndConquered нужно проверить)
        }
        event.getOriginal().getCapability(THIRD_HAND).ifPresent(old ->
                event.getEntity().getCapability(THIRD_HAND).ifPresent(neu -> {
                    neu.setStack(old.getStack().copy());
                })
        );
    }

    public interface IThirdHand {
        ItemStack getStack();
        void setStack(ItemStack stack);
        CompoundTag serializeNBT();
        void deserializeNBT(CompoundTag nbt);
    }

    public static class ThirdHandData implements IThirdHand, ICapabilitySerializable<CompoundTag> {
        private ItemStack stack = ItemStack.EMPTY;

        @Override
        public ItemStack getStack() { return stack; }

        @Override
        public void setStack(ItemStack stack) { this.stack = stack; }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            if (!stack.isEmpty()) {
                tag.put("stack", stack.save(new CompoundTag()));
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            if (nbt.contains("stack")) {
                stack = ItemStack.of(nbt.getCompound("stack"));
            } else {
                stack = ItemStack.EMPTY;
            }
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return THIRD_HAND.orEmpty(cap, LazyOptional.of(() -> this));
        }
    }
}
