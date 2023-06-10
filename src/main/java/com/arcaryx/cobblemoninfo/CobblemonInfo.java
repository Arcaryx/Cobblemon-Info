package com.arcaryx.cobblemoninfo;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Mod(CobblemonInfo.MOD_ID)
public class CobblemonInfo {
    public static final String MOD_ID = "cobblemoninfo";
    public static final Logger LOGGER = LogManager.getLogger();
    public static CobblemonInfo INSTANCE;

    public CobblemonInfo() {
        INSTANCE = this;
    }
}