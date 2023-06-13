package com.arcaryx.cobblemoninfo.net.message;

import com.arcaryx.cobblemoninfo.config.CommonConfig;
import net.minecraft.network.FriendlyByteBuf;

public class ConfigSyncMessage {
    private CommonConfig config;

    public CommonConfig getConfig() {
        return config;
    }

    public ConfigSyncMessage(CommonConfig config) {
        this.config = config;
    }

    public static void encode(ConfigSyncMessage message, FriendlyByteBuf buf) {
        buf.writeBoolean(message.config.modifyPokemonTooltip);
        buf.writeEnum(message.config.showPokemonGender);
        buf.writeEnum(message.config.showPokemonHealth);
        buf.writeEnum(message.config.showPokemonTrainer);
        buf.writeEnum(message.config.showPokemonTypes);
        buf.writeEnum(message.config.showPokemonRewardEvs);
        buf.writeEnum(message.config.showPokemonNature);
        buf.writeEnum(message.config.showPokemonAbility);
        buf.writeEnum(message.config.showPokemonIvs);
        buf.writeEnum(message.config.showPokemonEvs);
        buf.writeEnum(message.config.showPokemonDexEntry);
        buf.writeEnum(message.config.showHealerEnergy);
    }

    public static ConfigSyncMessage decode(FriendlyByteBuf buf) {
        CommonConfig config = new CommonConfig(null);
        config.modifyPokemonTooltip = buf.readBoolean();
        config.showPokemonGender = buf.readEnum(CommonConfig.ShowType.class);
        config.showPokemonHealth = buf.readEnum(CommonConfig.ShowType.class);
        config.showPokemonTrainer = buf.readEnum(CommonConfig.ShowType.class);
        config.showPokemonTypes = buf.readEnum(CommonConfig.ShowType.class);
        config.showPokemonRewardEvs = buf.readEnum(CommonConfig.ShowType.class);
        config.showPokemonNature = buf.readEnum(CommonConfig.ShowType.class);
        config.showPokemonAbility = buf.readEnum(CommonConfig.ShowType.class);
        config.showPokemonIvs = buf.readEnum(CommonConfig.ShowType.class);
        config.showPokemonEvs = buf.readEnum(CommonConfig.ShowType.class);
        config.showPokemonDexEntry = buf.readEnum(CommonConfig.ShowType.class);
        config.showHealerEnergy = buf.readEnum(CommonConfig.ShowType.class);

        return new ConfigSyncMessage(config);
    }
}