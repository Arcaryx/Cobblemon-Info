package com.arcaryx.cobblemoninfo.waila;

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

    public static boolean check(String name) {
        for (TooltipType t : TooltipType.values()) {
            if (t.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static final List<TooltipType> defaultShow = Arrays.asList(
            TooltipType.GENDER,
            TooltipType.HEALTH,
            TooltipType.TRAINER,
            TooltipType.FRIENDSHIP,
            TooltipType.TYPES,
            TooltipType.REWARD_EVS,
            TooltipType.NATURE,
            TooltipType.ABILITY,
            TooltipType.IVS,
            TooltipType.EVS
    );

    public static final List<TooltipType> defaultSneak = Arrays.asList(
            TooltipType.DEX_ENTRY
    );
}
