package com.example.thirdhand;

import com.example.thirdhand.capability.ThirdHandCapability;
import com.example.thirdhand.client.ThirdHandOverlay;
import com.example.thirdhand.network.ThirdHandNetworking;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ThirdHandMod.MODID)
public class ThirdHandMod {
    public static final String MODID = "thirdhand";

    public ThirdHandMod() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ThirdHandCapability.register();
        ThirdHandOverlay.register(modBus);
        ThirdHandNetworking.register();

        MinecraftForge.EVENT_BUS.register(this);
    }
}
