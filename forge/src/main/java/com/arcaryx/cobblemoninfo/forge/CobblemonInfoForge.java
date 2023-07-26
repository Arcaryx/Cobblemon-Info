package com.arcaryx.cobblemoninfo.forge;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod(CobblemonInfo.MOD_ID)
public class CobblemonInfoForge {

    private static final ForgeConfig config;
    private static final ForgeConfigSpec commonSpec;

    static {
        final Pair<ForgeConfig, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(ForgeConfig::new);
        config = common.getLeft();
        commonSpec = common.getRight();
    }

    public CobblemonInfoForge() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
        CobblemonInfo.CONFIG = config;
        CobblemonInfo.NETWORK = new ForgeNetworkHandler();
        CobblemonInfo.init();
    }
}