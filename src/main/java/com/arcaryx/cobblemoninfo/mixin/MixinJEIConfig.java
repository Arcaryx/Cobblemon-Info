package com.arcaryx.cobblemoninfo.mixin;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonFloatingState;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository;
import jeresources.api.drop.LootDrop;
import jeresources.compatibility.CompatBase;
import jeresources.entry.MobEntry;
import jeresources.jei.JEIConfig;
import jeresources.registry.MobRegistry;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JEIConfig.class)
public abstract class MixinJEIConfig {
    @Inject(method = "registerRecipes(Lmezz/jei/api/registration/IRecipeRegistration;)V",at=@At(value="HEAD"), remap = false, require = 0)
    private void registerRecipes(IRecipeRegistration registration, CallbackInfo ci) {
        var pokemonSpecies = PokemonSpecies.INSTANCE.getSpecies();
        int j = 0; // TODO: Remove (testing)
        for (var species : pokemonSpecies) {
            var drops = species.getDrops().getEntries();
            var lootDrops = new LootDrop[drops.size()];
            for (var i = 0; i < lootDrops.length; i++) {
                var drop = drops.get(i);
                // TODO: Remove (testing)
                lootDrops[i] = new LootDrop(Items.STICK.getDefaultInstance());
            }

            var entry = MobEntry.create(() -> {
                // TODO: Figure out how to animate this model
                var properties = new PokemonProperties();
                properties.setSpecies(species.getName());
                var entity = properties.createEntity(CompatBase.getLevel());
                var model = PokemonModelRepository.INSTANCE.getPoser(species.getResourceIdentifier(), entity.getAspects().get());
                model.setupAnimStateful(entity, new PokemonFloatingState(), 0, 0, 0, 0, 0);
                return model.getCurrentEntity();
            }, lootDrops);
            MobRegistry.getInstance().registerMob(entry);
            // TODO: Remove (testing)
            j++;
            if (j == 99)
                break;
        }




    }

}
