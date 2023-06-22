package com.arcaryx.cobblemoninfo.forge;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CobblemonInfo.MOD_ID)
public class CobblemonInfoForge {
    public CobblemonInfoForge() {
        CobblemonInfo.init();
    }
}