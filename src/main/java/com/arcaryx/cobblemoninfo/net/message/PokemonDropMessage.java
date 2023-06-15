package com.arcaryx.cobblemoninfo.net.message;

import com.arcaryx.cobblemoninfo.data.ClientCache;
import com.arcaryx.cobblemoninfo.data.PokemonDrop;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PokemonDropMessage {
    private final List<PokemonDrop> drops;

    public PokemonDropMessage(List<PokemonDrop> drops) {
        this.drops = drops;
    }

    public List<PokemonDrop> getDrops() {
        return drops;
    }

    public static void encode(PokemonDropMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getDrops().size());
        for (PokemonDrop drop : msg.getDrops()) {
            buf.writeResourceLocation(drop.getSpecies());
            buf.writeUtf(drop.getForm());
            buf.writeResourceLocation(drop.getItem());
            buf.writeFloat(drop.getChance());
            buf.writeInt(drop.getRange().getFirst());
            buf.writeInt(drop.getRange().getLast());
        }
    }

    public static PokemonDropMessage decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<PokemonDrop> drops = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ResourceLocation species = buf.readResourceLocation();
            String form = buf.readUtf();
            ResourceLocation item = buf.readResourceLocation();
            float chance = buf.readFloat();
            int min = buf.readInt();
            int max = buf.readInt();
            drops.add(new PokemonDrop(species, form, item, chance, min, max));
        }
        return new PokemonDropMessage(drops);
    }

    public static void handle(PokemonDropMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientCache.setPokemonDrops(msg.getDrops()));
        ctx.get().setPacketHandled(true);
    }
}
