package com.arcaryx.cobblemoninfo.mixin.jer;

import jeresources.fabric.ModInfo;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(value = ModInfo.class)
public abstract class MixinModInfo {

    @Shadow
    private ModContainer modContainer;

    @Inject(method = "getPackResources()Lnet/minecraft/server/packs/PackResources;", at=@At(value="HEAD"), cancellable = true, require = 0)
    private void fixGetPackResources(CallbackInfoReturnable<PackResources> cir) {
        // TODO: Temp fix until I have time to find a real solution and submit a PR to JER
        var path = this.modContainer.getRoot().toUri().toString().substring(12, this.modContainer.getRoot().toUri().toString().length() - 2);
        var file = new File(path);
        var pack = new FilePackResources(file);
        cir.setReturnValue(pack);
    }

}
