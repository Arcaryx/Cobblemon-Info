package com.arcaryx.cobblemoninfo;

import com.arcaryx.cobblemoninfo.event.EventHandler;
import com.arcaryx.cobblemoninfo.net.NetworkHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobblemonInfo implements ModInitializer {
	public static final String MOD_ID = "cobblemoninfo";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static CobblemonInfo INSTANCE;

	public CobblemonInfo() {
		INSTANCE = this;
	}

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
			NetworkHandler.registerClient();
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.player;
			EventHandler.onDatapackSync(player);
		});
	}
}