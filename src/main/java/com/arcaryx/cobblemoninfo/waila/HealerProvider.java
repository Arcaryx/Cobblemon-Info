package com.arcaryx.cobblemoninfo.waila;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.CommonConfig;
import com.cobblemon.mod.common.block.entity.HealingMachineBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;

public enum HealerProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        var showHealer = CobblemonInfo.config.showHealerEnergy;
        if (showHealer != CommonConfig.ShowType.SHOW && (showHealer == CommonConfig.ShowType.SNEAK && !accessor.getPlayer().isCrouching()))
            return;
        if (!(accessor.getBlockEntity() instanceof HealingMachineBlockEntity healer))
            return;
        var current = healer.getHealingCharge();
        var max = healer.getMaxCharge();
        var percentage = current / max;
        IElementHelper helper = IElementHelper.get();
        var progressStyle = helper.progressStyle().color(-16777114, -16777046);
        tooltip.add(helper.progress(percentage,
                Component.literal("Charge: %d%%".formatted(Math.round(percentage * 100)))
                        .withStyle(ChatFormatting.GRAY), progressStyle, BoxStyle.DEFAULT, true));
    }

    @Override
    public ResourceLocation getUid() {
        return CobblemonWailaPlugin.HEALER;
    }
}
