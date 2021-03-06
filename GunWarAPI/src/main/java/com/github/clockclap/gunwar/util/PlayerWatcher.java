/*
 * Copyright (c) 2021, ClockClap. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.github.clockclap.gunwar.util;

import com.github.clockclap.gunwar.GunWar;
import com.github.clockclap.gunwar.GwAPI;
import com.github.clockclap.gunwar.game.GameState;
import com.github.clockclap.gunwar.game.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

@GwAPI
public class PlayerWatcher {

    private final Plugin plugin;
    private final PlayerData data;
    private BukkitRunnable runnable1tick;
    private BukkitRunnable runnable10tick;
    private Scoreboard scoreboard;
    private Objective objective;

    public PlayerWatcher(Plugin plugin, PlayerData data) {
        this.plugin = plugin;
        this.data = data;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective(data.detail().getOriginalName(), "dummy");
        this.objective.setDisplayName(TextReference.SIDEBAR_TITLE);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public PlayerData getOwner() {
        return data;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public Objective getObjective() {
        return this.objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }


    public void startWatch10Ticks() {
        runnable10tick = new BukkitRunnable() {
            @Override
            public void run() {
                getObjective().unregister();
                setObjective(getScoreboard().registerNewObjective(getOwner().detail().getOriginalName(), "dummy"));
                getObjective().setDisplayName(TextReference.SIDEBAR_TITLE);
                getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);
                if(GunWar.getGame().getState() == GameState.WAITING) {
                    List<String> lines = new ArrayList<>();
                    lines.add("");
                    lines.add(TextReference.SIDEBAR_GAMESTATE + ": " + TextReference.SIDEBAR_GAMESTATE_WAITING);
                    if(GunWar.getPluginConfigs().getConfig().getBoolean("game.auto-start", false)) {
                        lines.add(TextReference.SIDEBAR_WAITING_PLAYER
                                .replaceAll("%PLAYERS%",
                                        (Math.max(
                                                0,
                                                GunWar.getPluginConfigs().getConfig().getInt("required-players", 10)
                                                - Bukkit.getOnlinePlayers().size())
                                        )
                                                + ""));
                    } else {
                        if (getOwner().getPlayer().isOp()) {
                            lines.add(TextReference.SIDEBAR_PLEASE_START);
                        } else {
                            lines.add(TextReference.SIDEBAR_PLEASE_WAIT);
                        }
                    }
                    lines.add(ChatColor.RESET + "");
                    lines.add(TextReference.SIDEBAR_PLAYERS + ": " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + ChatColor.RESET + "/" + ChatColor.GREEN + Bukkit.getMaxPlayers());
                    lines.add(ChatColor.WHITE + "");
                    lines.add(ChatColor.GOLD + "nanami-network");
                    int i = lines.size();
                    for(String line : lines) {
                        getObjective().getScore(line).setScore(i);
                        i--;
                    }

                    getOwner().getPlayer().setScoreboard(getScoreboard());
                }
                if(GunWar.getGame().getState() == GameState.STARTING) {
                    List<String> lines = new ArrayList<>();
                    lines.add("");
                    lines.add(TextReference.SIDEBAR_GAMESTATE + ": " + TextReference.SIDEBAR_GAMESTATE_WAITING);
                    lines.add(TextReference.SIDEBAR_STARTING_AT.replaceAll("%SECOND%", GunWar.getGame().getStartingAt() + ""));
                    lines.add(ChatColor.RESET + "");
                    lines.add(TextReference.SIDEBAR_PLAYERS + ": " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + ChatColor.RESET + "/" + ChatColor.GREEN + Bukkit.getMaxPlayers());
                    lines.add(ChatColor.WHITE + "");
                    lines.add(ChatColor.GOLD + "nanami-network");
                    int i = lines.size();
                    for(String line : lines) {
                        getObjective().getScore(line).setScore(i);
                        i--;
                    }

                    getOwner().getPlayer().setScoreboard(getScoreboard());
                }
            }
        };
        runnable10tick.runTaskTimer(plugin, 0, 10);
    }

    public void startWatch() {
        startWatch10Ticks();
        runnable1tick = new BukkitRunnable() {
            @Override
            public void run() {

            }
        };
        runnable1tick.runTaskTimer(plugin, 0, 1);
    }

    public void stopWatch10Ticks() {
        runnable10tick.cancel();
    }

    public void stopWatch() {
        runnable1tick.cancel();
    }

}
