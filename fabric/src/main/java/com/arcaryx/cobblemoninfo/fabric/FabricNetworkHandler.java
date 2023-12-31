package com.arcaryx.cobblemoninfo.fabric;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.net.AbstractNetworkHandler;
import com.arcaryx.cobblemoninfo.net.messages.AbstractMessage;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class FabricNetworkHandler extends AbstractNetworkHandler {
    @Override
    public <T extends AbstractMessage> void registerMessageClient(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder) {
        var location = new ResourceLocation(CobblemonInfo.MOD_ID, type.getSimpleName().toLowerCase());
        ClientPlayNetworking.registerGlobalReceiver(location, (client, handler, buf, responseSender) -> {
            AbstractMessage message = decoder.apply(buf);
            message.handle(null);
        });
    }

    @Override
    public void sendToPlayer(ServerPlayer player, AbstractMessage message) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        message.encode(buf);
        var location = new ResourceLocation(CobblemonInfo.MOD_ID, message.getClass().getSimpleName().toLowerCase());
        ServerPlayNetworking.send(player, location, buf);
    }
}
