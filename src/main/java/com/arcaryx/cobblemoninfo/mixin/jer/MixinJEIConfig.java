package com.arcaryx.cobblemoninfo.mixin.jer;

import com.arcaryx.cobblemoninfo.data.ClientCache;
import com.cobblemon.mod.common.api.drop.ItemDropEntry;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import jeresources.api.conditionals.LightLevel;
import jeresources.api.drop.LootDrop;
import jeresources.compatibility.CompatBase;
import jeresources.entry.MobEntry;
import jeresources.jei.JEIConfig;
import jeresources.registry.MobRegistry;
import kotlin.ranges.IntRange;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JEIConfig.class)
public abstract class MixinJEIConfig {
    @Inject(method = "registerRecipes(Lmezz/jei/api/registration/IRecipeRegistration;)V",at=@At(value="HEAD"), remap = false, require = 0)
    private void registerRecipes(IRecipeRegistration registration, CallbackInfo ci) {
        var pokemonSpecies = PokemonSpecies.INSTANCE.getSpecies();
        for (var species : pokemonSpecies) {
            var drops = ClientCache.getPokemonDrops(species);
            var lootDrops = new LootDrop[drops.size()];
            for (var i = 0; i < lootDrops.length; i++) {
                var drop = drops.get(i);
                var item = ForgeRegistries.ITEMS.getValue(drop.getItem());
                if (item != null) {
                    var range = drop.getRange();
                    lootDrops[i] = new LootDrop(item.getDefaultInstance(), range.getFirst(), range.getLast(), drop.getChance());
                }
            }

            var entry = MobEntry.create(() -> {
                // TODO: Figure out how to set the pose (not t-posing)
                var properties = new PokemonProperties();
                properties.setSpecies(species.getName());
                return properties.createEntity(CompatBase.getLevel());
            }, LightLevel.any, 0, lootDrops);
            MobRegistry.getInstance().registerMob(entry);
        }
    }
}
