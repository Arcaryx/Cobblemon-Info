package com.arcaryx.cobblemoninfo.fabric;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import net.fabricmc.api.ModInitializer;

public class CobblemonInfoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CobblemonInfo.init();
    }
}