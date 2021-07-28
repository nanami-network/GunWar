package xyz.n7mn.dev.gunwar.achievement;

import xyz.n7mn.dev.gunwar.achievement.achievements.CraftAchievementMasterKiller;

import java.util.ArrayList;
import java.util.List;

public final class GwAchievementDefaults {

    public static List<GwAchievement> defaultAchievements = new ArrayList<>();

    public static void init() {
        defaultAchievements.add(new CraftAchievementMasterKiller());
    }

    public static void register() {
        for(GwAchievement a : defaultAchievements) GwAchievementRegisterer.register(a);
    }

    public static List<GwAchievement> getDefaultAchievements() {
        return defaultAchievements;
    }

}
