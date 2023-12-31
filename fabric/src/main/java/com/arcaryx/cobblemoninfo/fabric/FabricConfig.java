package com.arcaryx.cobblemoninfo.fabric;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.IConfig;
import com.arcaryx.cobblemoninfo.config.ShowType;
import com.arcaryx.cobblemoninfo.waila.TooltipType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class FabricConfig implements IConfig {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(new TypeToken<Pair<TooltipType, ShowType>>(){}.getType(), new PairAdapter())
            .create();
    private static final Path CONFIG_PATH = Paths.get("config", CobblemonInfo.MOD_ID + ".json");

    private boolean hidePokemonLabel;
    private List<Pair<TooltipType, ShowType>> pokemonTooltips = TooltipType.pokemonDefaults;

    public static FabricConfig load() {
        var config = new FabricConfig();
        if (!Files.exists(CONFIG_PATH)) {
            config.hidePokemonLabel = FabricLoader.getInstance().isModLoaded("jade");
            config.saveConfig();
        } else {
            config.loadConfig();
        }
        return config;
    }

    private void loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
            FabricConfig config = GSON.fromJson(reader, FabricConfig.class);
            this.hidePokemonLabel = config.hidePokemonLabel;
            this.pokemonTooltips = config.pokemonTooltips;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hidePokemonLabel() {
        return hidePokemonLabel;
    }

    @Override
    public List<Pair<TooltipType, ShowType>> getPokemonTooltips() {
        return Collections.unmodifiableList(pokemonTooltips);
    }
}