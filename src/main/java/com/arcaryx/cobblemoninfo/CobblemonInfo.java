package com.arcaryx.cobblemoninfo;

import com.arcaryx.cobblemoninfo.config.CommonConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CobblemonInfo.MOD_ID)
public class CobblemonInfo {
    public static final String MOD_ID = "cobblemoninfo";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec commonSpec;
    public static CobblemonInfo INSTANCE;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON = common.getLeft();
        commonSpec = common.getRight();
    }

    public CobblemonInfo() {
        INSTANCE = this;

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
    }
}