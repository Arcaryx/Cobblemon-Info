package com.arcaryx.cobblemoninfo.waila;

import com.arcaryx.cobblemoninfo.config.ShowType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public enum TooltipType {
    GENDER,
    HEALTH,
    TRAINER,
    FRIENDSHIP,
    TYPES,
    REWARD_EVS,
    NATURE,
    ABILITY,
    IVS,
    EVS,
    DEX_ENTRY,
    HEALER_ENERGY,
    APRICORN_GROWTH;


    public static boolean check(Object entry) {
        if (!(entry instanceof String entryStr)) {
            return false;
        }
        var str = entryStr.split(":");
        if (str.length != 2) {
            return false;
        }
        if (Arrays.stream(TooltipType.values()).noneMatch(x -> str[0].equals(x.name()))) {
            return false;
        }
        return Arrays.stream(ShowType.values()).anyMatch(x -> str[1].equals(x.name()));
    }

    public static final List<Pair<TooltipType, ShowType>> defaults = Arrays.asList(
            Pair.of(TooltipType.GENDER, ShowType.SHOW),
            Pair.of(TooltipType.HEALTH, ShowType.SHOW),
            Pair.of(TooltipType.TRAINER, ShowType.SHOW),
            Pair.of(TooltipType.FRIENDSHIP, ShowType.SHOW),
            Pair.of(TooltipType.TYPES, ShowType.SHOW),
            Pair.of(TooltipType.REWARD_EVS, ShowType.SHOW),
            Pair.of(TooltipType.NATURE, ShowType.SHOW),
            Pair.of(TooltipType.ABILITY, ShowType.SHOW),
            Pair.of(TooltipType.IVS, ShowType.SHOW),
            Pair.of(TooltipType.EVS, ShowType.SHOW),
            Pair.of(TooltipType.DEX_ENTRY, ShowType.SNEAK)
    );
}
