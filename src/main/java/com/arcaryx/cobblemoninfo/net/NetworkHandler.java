package com.arcaryx.cobblemoninfo.net;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.data.ClientCache;
import com.arcaryx.cobblemoninfo.net.message.PokemonDropMessage;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class NetworkHandler {
    public static final ResourceLocation PACKET_ID = new ResourceLocation(CobblemonInfo.MOD_ID, "pokemon_drop_message");

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(PACKET_ID, (client, handler, buf, responseSender) -> {
            var drops = PokemonDropMessage.decode(buf).getDrops();
            client.execute(() -> ClientCache.setPokemonDrops(drops));
        });
    }

    public static void sendToPlayer(ServerPlayer player, PokemonDropMessage msg) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        PokemonDropMessage.encode(msg, buf);
        ServerPlayNetworking.send(player, PACKET_ID, buf);
    }
}