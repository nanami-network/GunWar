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

package com.github.clockclap.gunwar.commands;

import com.github.clockclap.gunwar.GunWarCommand;
import com.github.clockclap.gunwar.GwPlugin;
import com.github.clockclap.gunwar.LoggableDefault;
import com.github.clockclap.gunwar.util.PermissionInfo;
import com.github.clockclap.gunwar.util.TextReference;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@GwPlugin
public class GunWarConfigCommand extends GunWarCommand implements LoggableDefault {

    public GunWarConfigCommand() {
        super("gunwarconfig");
        setAliases(Arrays.asList("gunwarconf", "gwconfig", "gwconf"));
        setDescription("銃撃戦プラグインの設定を確認できます。");
        setUsage("Usage: /gunwarconfig");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            int required = getRequiredPermission("commands.gunwarconfig", 1);
            PermissionInfo info = testPermission(p, required);
            if(!info.isPassed()) {
                p.sendMessage(TextReference.getChatCommandPermissionError(info.getRequired(), info.getCurrent()));
                return true;
            }
        }
        if(args.length <= 1) {
            sender.sendMessage("" +
                    ChatColor.DARK_GREEN + "=-=-=-=- 銃撃戦 -=-=-=-=" + "\n" +
                    ChatColor.YELLOW + "自動でゲーム開始: " + ChatColor.GREEN +
                    (getPluginConfigs().getConfig().getBoolean("game.auto-start", true) ? "有効" : ChatColor.RED + "無効") + "\n" +
                    ChatColor.YELLOW + "ゲームモード: " + ChatColor.RED + getPluginConfigs().getConfig().getString("game.gamemode", "NORMAL") + "\n" +
                    ChatColor.GOLD + "デバッグモード: " + ChatColor.GREEN +
                    (getPluginConfigs().getConfig().getBoolean("debug", false) ? "有効" : ChatColor.RED + "無効") + "\n" +
                    ChatColor.DARK_GREEN + "=-=-=-=-=-=-=-=-=-=-=-=");
        }
        if(args.length > 1) {
            if(args[1].equalsIgnoreCase("no") || args[1].equalsIgnoreCase("normal")) {
                sender.sendMessage("" +
                        ChatColor.DARK_GREEN + "=-=-=-=- 銃撃戦 (Normal) -=-=-=-=" + "\n" +
                        ChatColor.DARK_GREEN + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            }
            if(args[1].equalsIgnoreCase("ze") || args[1].equalsIgnoreCase("zombie-escape") || args[1].equalsIgnoreCase("zombieescape")) {
                sender.sendMessage("" +
                        ChatColor.DARK_GREEN + "=-=-=-=- 銃撃戦 (Zombie Escape) -=-=-=-=" + "\n" +
                        ChatColor.DARK_GREEN + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            }
        }
        return true;
    }
}
