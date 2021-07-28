package xyz.n7mn.dev.gunwar.game.data;

import xyz.n7mn.dev.gunwar.achievement.GwAchievement;
import xyz.n7mn.dev.gunwar.item.GwGunItem;
import xyz.n7mn.dev.gunwar.item.GwItem;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PermanentlyPlayerData {

    public UUID getUniqueId();

    public int getCoins();

    public List<GwItem> getItemInProcessions();

    public List<GwItem> getGifts();

    public List<GwAchievement> getAchievements();

    public Map<GwGunItem, Integer> getPlayCount();

    public Map<GwGunItem, Integer> getKillCount();

    public int getDeathCount();

    public int getInfectedCount();

    public void setCoins(int coins);

    public void setDeathCount(int deathCount);

    public void setInfectedCount(int infectedCount);

    public File getDefaultDataFile();

    public void save(File file) throws IOException;

    public void load(File file) throws IOException,ClassNotFoundException;

}
