package com.arcaryx.cobblemoninfo.data;

import kotlin.ranges.IntRange;
import net.minecraft.resources.ResourceLocation;

public class PokemonDrop {
    private final ResourceLocation species, item;
    private final float chance;
    private final int min, max;

    public PokemonDrop(ResourceLocation species, ResourceLocation item, float chance, int min, int max) {
        this.species = species;
        this.item = item;
        this.chance = chance;
        this.min = min;
        this.max = max;
    }

    public ResourceLocation getSpecies() {
        return species;
    }

    public ResourceLocation getItem() {
        return item;
    }

    public float getChance() {
        return chance;
    }

    public IntRange getRange() {
        return new IntRange(min, max);
    }
}