package xyz.n7mn.dev.gunwar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.n7mn.dev.gunwar.commands.AboutGunWarCommand;
import xyz.n7mn.dev.gunwar.commands.GunWarItemCommand;
import xyz.n7mn.dev.gunwar.commands.GunWarReloadCommand;
import xyz.n7mn.dev.gunwar.game.GameState;
import xyz.n7mn.dev.gunwar.game.GunWarGame;
import xyz.n7mn.dev.gunwar.game.data.GunWarPermanentlyPlayerData;
import xyz.n7mn.dev.gunwar.game.data.GunWarPlayerData;
import xyz.n7mn.dev.gunwar.game.data.PermanentlyPlayerData;
import xyz.n7mn.dev.gunwar.game.data.PlayerData;
import xyz.n7mn.dev.gunwar.game.gamemode.GwGameModes;
import xyz.n7mn.dev.gunwar.item.GwItems;
import xyz.n7mn.dev.gunwar.listeners.DamageListener;
import xyz.n7mn.dev.gunwar.listeners.ItemListener;
import xyz.n7mn.dev.gunwar.listeners.PlayerListener;
import xyz.n7mn.dev.gunwar.listeners.ServerListener;
import xyz.n7mn.dev.gunwar.mysql.GwMySQLDataPath;
import xyz.n7mn.dev.gunwar.mysql.GwMySQLPlayerDataUpdater;
import xyz.n7mn.dev.gunwar.util.GwUtilities;
import xyz.n7mn.dev.gunwar.util.NanamiGunWarConfiguration;
import xyz.n7mn.dev.gunwar.util.PlayerWatcher;
import xyz.n7mn.dev.gunwar.util.Reference;

import java.io.File;
import java.io.IOException;

public final class NanamiGunWar extends JavaPlugin {

    private NanamiGunWar plugin;
    private SimplePluginManager pluginManager;
    private GwUtilities utilities;
    private NanamiGunWarConfiguration config;
    public static GwMySQLPlayerDataUpdater updater;
    private GunWarGame game;
    private BukkitRunnable runnable;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        GunWar.plugin = plugin;
        utilities = new GwUtilities(plugin);
        GunWar.utilities = utilities;
        config = new NanamiGunWarConfiguration(plugin);
        GunWar.config = config;
        config.init();
        GwGameModes.registerDefault();
        game = new GunWarGame(plugin);
        GunWar.game = game;
        GwItems.a();
        pluginManager = (SimplePluginManager) Bukkit.getPluginManager();
        bossBar();
        registerListeners();
        registerCommands();
        for(Player p : Bukkit.getOnlinePlayers()) {
            try {
                GunWarPermanentlyPlayerData permanentlyPlayerData = new GunWarPermanentlyPlayerData(p.getUniqueId());
                File f = permanentlyPlayerData.getDefaultDataFile();
                if (!f.exists()) {
                    permanentlyPlayerData.save(f);
                } else {
                    permanentlyPlayerData.load(f);
                }
                ((GunWarGame) GunWar.getGame()).addPermanentlyPlayerData(permanentlyPlayerData);
            } catch(Throwable ex) {
                p.kickPlayer("" +
                        ChatColor.DARK_GREEN + "=== ななみ銃撃戦 ===\n" +
                        ChatColor.RED + "接続が切れました。\n" +
                        ChatColor.GRAY + "原因: " + ChatColor.WHITE + "プレイヤーデータ読み込み時のエラー発生\n" +
                        ChatColor.GRAY + "解決策: " + ChatColor.WHITE + "Discordの" + ChatColor.BLUE + "#銃撃戦-バグ報告" + ChatColor.WHITE + "にて報告してください。\n" +
                        "\n" +
                        ChatColor.WHITE + "詳細はななみ鯖公式Discordをご確認ください。\n" +
                        ChatColor.GOLD + "" + ChatColor.UNDERLINE + GunWar.getConfig().getConfig().getString("discord"));
                ex.printStackTrace();
            }
            GunWarPlayerData data = new GunWarPlayerData(p);
            PlayerWatcher watcher = new PlayerWatcher(GunWar.getPlugin(), data);
            watcher.startWatch();
            watcher.startWatch10Ticks();
            data.setWatcher(watcher);
            ((GunWarGame) GunWar.getGame()).addPlayerData(data.getUniqueId(), data);
        }
        startWatch();
    }

    private void bossBar() {
        BossBar bar = Bukkit.getServer().createBossBar(Reference.BOSSBAR_WAITING, BarColor.WHITE, BarStyle.SOLID);
        bar.setVisible(true);
        bar.setProgress(1.0);
        game.setBar(bar);
    }

    private void registerListeners() {
        pluginManager.registerEvents(new PlayerListener(), plugin);
        pluginManager.registerEvents(new ServerListener(), plugin);
        pluginManager.registerEvents(new ItemListener(), plugin);
        pluginManager.registerEvents(new DamageListener(), plugin);
    }

    private void registerCommands() {
        utilities.registerCommand(plugin.getName(), new GunWarReloadCommand());
        utilities.registerCommand(plugin.getName(), new GunWarItemCommand());
    }

    private void startWatch() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                int currentPlayers = Bukkit.getOnlinePlayers().size();
                int requiredPlayers = GunWar.getConfig().getConfig().getInt("game.required-players", 10);
                if(GunWar.getGame().getState() == GameState.WAITING && requiredPlayers <= currentPlayers) {
                    GunWar.getGame().start();
                }
            }
        };
        runnable.runTaskTimer(this, 0, 5);
    }

    private void cancelWatch() {
        if(runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        cancelWatch();
        for(Player p : Bukkit.getOnlinePlayers()) {
            PlayerData data = GunWar.getGame().getPlayerData(p);
            if(data != null) {
                PlayerWatcher watcher = data.getWatcher();
                watcher.stopWatch();
                watcher.stopWatch10Ticks();
                ((GunWarGame) GunWar.getGame()).removePlayerData(data.getUniqueId());
            }
            PermanentlyPlayerData data_ = GunWar.getGame().getPermanentlyPlayerData(p.getUniqueId());
            if(data_ != null) {
                try {
                    data_.save(data_.getDefaultDataFile());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ((GunWarGame) GunWar.getGame()).removePermanentlyPlayerData(p.getUniqueId());
            }
        }
        GwGameModes.clear();
        GwItems.clear();
    }
}
