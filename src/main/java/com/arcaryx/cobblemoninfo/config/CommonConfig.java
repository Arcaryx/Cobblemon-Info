package com.arcaryx.cobblemoninfo.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class CommonConfig {

    public enum ShowType {
        HIDE,
        SNEAK,
        SHOW
    }

    public boolean modifyPokemonTooltip;
    public ShowType showPokemonGender, showPokemonHealth, showPokemonTrainer, showPokemonFriendship,
            showPokemonTypes, showPokemonRewardEvs, showPokemonNature, showPokemonAbility, showPokemonIvs, showPokemonEvs, showPokemonDexEntry;
    public ShowType showHealerEnergy, showApricornProgress;
    public boolean hideNonPokemonDrops;

    private final Path configPath;

    public CommonConfig(Path configPath) {
        this.configPath = configPath;
        if (configPath != null)
            loadConfig();
    }

    private void loadConfig() {
        Properties properties = new Properties();

        try (InputStream inputStream = Files.newInputStream(configPath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Failed to load config, using default values.");
        }

        modifyPokemonTooltip = Boolean.parseBoolean(properties.getProperty("modifyPokemonTooltip", "true"));
        showPokemonGender = ShowType.valueOf(properties.getProperty("showPokemonGender", ShowType.SHOW.name()));
        showPokemonHealth = ShowType.valueOf(properties.getProperty("showPokemonHealth", ShowType.SHOW.name()));
        showPokemonTrainer = ShowType.valueOf(properties.getProperty("showPokemonTrainer", ShowType.SHOW.name()));
        showPokemonFriendship = ShowType.valueOf(properties.getProperty("showPokemonFriendship", ShowType.SHOW.name()));
        showPokemonTypes = ShowType.valueOf(properties.getProperty("showPokemonTypes", ShowType.SHOW.name()));
        showPokemonRewardEvs = ShowType.valueOf(properties.getProperty("showPokemonRewardEvs", ShowType.SHOW.name()));
        showPokemonNature = ShowType.valueOf(properties.getProperty("showPokemonNature", ShowType.SHOW.name()));
        showPokemonAbility = ShowType.valueOf(properties.getProperty("showPokemonAbility", ShowType.SHOW.name()));
        showPokemonIvs = ShowType.valueOf(properties.getProperty("showPokemonIvs", ShowType.SHOW.name()));
        showPokemonEvs = ShowType.valueOf(properties.getProperty("showPokemonEvs", ShowType.SHOW.name()));
        showPokemonDexEntry = ShowType.valueOf(properties.getProperty("showPokemonDexEntry", ShowType.SNEAK.name()));
        showHealerEnergy = ShowType.valueOf(properties.getProperty("showHealerEnergy", ShowType.SHOW.name()));
        showApricornProgress = ShowType.valueOf(properties.getProperty("showApricornProgress", ShowType.SHOW.name()));
        hideNonPokemonDrops = Boolean.parseBoolean(properties.getProperty("hideNonPokemonDrops", "false"));

        saveConfig();
    }

    public void saveConfig() {
        Properties properties = new Properties();

        properties.setProperty("modifyPokemonTooltip", Boolean.toString(modifyPokemonTooltip));
        properties.setProperty("showPokemonGender", showPokemonGender.name());
        properties.setProperty("showPokemonHealth", showPokemonHealth.name());
        properties.setProperty("showPokemonTrainer", showPokemonTrainer.name());
        properties.setProperty("showPokemonFriendship", showPokemonFriendship.name());
        properties.setProperty("showPokemonTypes", showPokemonTypes.name());
        properties.setProperty("showPokemonRewardEvs", showPokemonRewardEvs.name());
        properties.setProperty("showPokemonNature", showPokemonNature.name());
        properties.setProperty("showPokemonAbility", showPokemonAbility.name());
        properties.setProperty("showPokemonIvs", showPokemonIvs.name());
        properties.setProperty("showPokemonEvs", showPokemonEvs.name());
        properties.setProperty("showPokemonDexEntry", showPokemonDexEntry.name());
        properties.setProperty("showHealerEnergy", showHealerEnergy.name());
        properties.setProperty("showApricornProgress", showApricornProgress.name());
        properties.setProperty("hideNonPokemonDrops", Boolean.toString(hideNonPokemonDrops));

        try (OutputStream outputStream = Files.newOutputStream(configPath)) {
            properties.store(outputStream, "Fabric config for the mod");
        } catch (IOException e) {
            System.err.println("Failed to save config.");
        }
    }
}
