package com.arcaryx.cobblemoninfo.net;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.CommonConfig;
import com.arcaryx.cobblemoninfo.data.ClientCache;
import com.arcaryx.cobblemoninfo.net.message.ConfigSyncMessage;
import com.arcaryx.cobblemoninfo.net.message.PokemonDropMessage;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class NetworkHandler {
    public static final ResourceLocation POKEMON_DROPS = new ResourceLocation(CobblemonInfo.MOD_ID, "pokemon_drops");
    public static final ResourceLocation CONFIG_SYNC = new ResourceLocation(CobblemonInfo.MOD_ID, "config_sync");

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(POKEMON_DROPS, (client, handler, buf, responseSender) -> {
            var drops = PokemonDropMessage.decode(buf).getDrops();
            client.execute(() -> ClientCache.setPokemonDrops(drops));
        });

        ClientPlayNetworking.registerGlobalReceiver(CONFIG_SYNC, (client, handler, buf, responseSender) -> {
            var configMessage = ConfigSyncMessage.decode(buf);
            client.execute(() -> CobblemonInfo.config = configMessage.getConfig());
        });
    }

    public static void sendDropsToPlayer(ServerPlayer player, PokemonDropMessage msg) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        PokemonDropMessage.encode(msg, buf);
        ServerPlayNetworking.send(player, POKEMON_DROPS, buf);
    }

    public static void sendConfigToPlayer(ServerPlayer player, CommonConfig config) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        ConfigSyncMessage.encode(new ConfigSyncMessage(config), buf);
        ServerPlayNetworking.send(player, CONFIG_SYNC, buf);
    }
}