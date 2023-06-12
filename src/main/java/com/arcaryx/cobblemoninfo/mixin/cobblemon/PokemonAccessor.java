package com.arcaryx.cobblemoninfo.mixin.cobblemon;

import com.cobblemon.mod.common.pokemon.Pokemon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Pokemon.class, remap = false)
public interface PokemonAccessor {
    @Accessor("isClient")
    void setIsClient(boolean value);
}
