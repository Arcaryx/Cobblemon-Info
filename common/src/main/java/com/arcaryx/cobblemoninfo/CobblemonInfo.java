package com.arcaryx.cobblemoninfo;

import com.arcaryx.cobblemoninfo.config.IConfig;
import com.arcaryx.cobblemoninfo.net.AbstractNetworkHandler;

public class CobblemonInfo
{
	public static final String MOD_ID = "cobblemoninfo";
	public static IConfig CONFIG;
	public static AbstractNetworkHandler NETWORK;

	public static void init() {
		NETWORK.registerMessages();
	}
}