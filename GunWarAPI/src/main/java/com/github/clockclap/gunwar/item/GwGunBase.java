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

package com.github.clockclap.gunwar.item;

import com.github.clockclap.gunwar.GwAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@GwAPI
public abstract class GwGunBase extends GwWeaponBase implements GwGunItem {

    private int ammo;
    private double range;
    private float recoil;
    private float recoilSneaking;
    private long reload;
    private long fire;
    private float zoom;
    private float damageHeadShot;
    private float damageAimed;
    private float accuracy;
    private float accuracySneaking;
    private float knockback;
    private GwGunType gunType;
    private GunReloadingType type;

    protected GwGunBase() {
        this(0, Material.STONE, "", "", "",
                new ArrayList<>(), 0F, 0F, 0F, 0, 0,
                0F, 0F, 0L, 0L, 0F, 0F, 0F, 0F,  GwGunType.ASSAULT_RIFLE, GunReloadingType.ONCE);
    }
    protected GwGunBase(int index, Material type, String name, String displayName, String id, List<String> description, float damage, float damageAimed, float damageHeadShot,
                        int ammo, double range, float recoil, float recoilSneaking, long reload, long fire, float zoom, float accuracy,
                        float accuracySneaking, float knockback, GwGunType gunType) {
        this(index, type, name, displayName, id, description, damage, damageAimed, damageHeadShot, ammo, range, recoil, recoilSneaking, reload, fire, zoom, accuracy, accuracySneaking, knockback, gunType, GunReloadingType.ONCE);
    }

    protected GwGunBase(int index, Material type, String name, String displayName, String id, List<String> description, float damage, float damageAimed, float damageHeadShot,
                        int ammo, double range, float recoil, float recoilSneaking, long reload, long fire, float zoom, float accuracy,
                        float accuracySneaking, float knockback, GwGunType gunType, GunReloadingType reloadingType) {
        super(index, type, name, displayName, id, description, damage, WeaponType.GUN);
        this.ammo = ammo;
        this.range = range;
        this.recoil = recoil;
        this.recoilSneaking = recoilSneaking;
        this.reload = reload;
        this.fire = fire;
        this.zoom = zoom;
        this.damageHeadShot = damageHeadShot;
        this.damageAimed = damageAimed;
        this.accuracy = accuracy;
        this.accuracySneaking = accuracySneaking;
        this.knockback = knockback;
        this.gunType = gunType;
        this.type = reloadingType;
        a();
    }

    public int getAmmo() {
        return ammo;
    }

    public double getRange() {
        return range;
    }

    public float getRecoil() {
        return recoil;
    }

    public float getRecoilOnSneak() {
        return recoilSneaking;
    }

    public long getReload() {
        return reload;
    }

    public long getFire() {
        return fire;
    }

    public float getZoomLevel() {
        return zoom;
    }

    public float getHeadShotDamage() {
        return damageHeadShot;
    }

    public float getDamageAimed() {
        return damageAimed;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public float getAccuracyOnSneak() {
        return accuracySneaking;
    }

    public float getKnockBack() {
        return knockback;
    }

    public GwGunType getGunType() {
        return gunType;
    }

    public GunReloadingType getReloadingType() {
        return type;
    }

    protected void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    protected void setRange(float range) {
        this.range = range;
    }

    protected void setRecoil(float recoil) {
        this.recoil = recoil;
    }

    protected void setRecoilOnSneak(float recoilSneaking) {
        this.recoilSneaking = recoilSneaking;
    }

    protected void setReload(long reload) {
        this.reload = reload;
    }

    protected void setFire(long fire) {
        this.fire = fire;
    }

    protected void setZoomLevel(float zoom) {
        this.zoom = zoom;
    }

    protected void setHeadShotDamage(float damageHeadShot) {
        this.damageHeadShot = damageHeadShot;
    }

    protected void setDamageAimed(float damageAimed) {
        this.damageAimed = damageAimed;
    }

    protected void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    protected void setAccuracyOnSneak(float accuracySneaking) {
        this.accuracySneaking = accuracySneaking;
    }

    protected void setKnockBack(float knockback) {
        this.knockback = knockback;
    }

    protected void setGunType(GwGunType gunType) {
        this.gunType = gunType;
    }
    
    protected void setReloadingType(GunReloadingType type) {
        this.type = type;
    }

    protected void setDescription(List<String> description) {
        this.description = description;
        a();
    }

    protected void setId(String id) {
        this.id = id;
        a();
    }

    private void a() {
        ItemMeta meta = getItem().getItemMeta();
        List<String> lore = new ArrayList<>(getDescription());
        String guntype = "";
        switch (getGunType()) {
            case ASSAULT_RIFLE:
                guntype = "Assault Rifle";
                break;
            case SNIPER_RIFLE:
                guntype = "Sniper Rifle";
                break;
            case SUBMACHINE_GUN:
                guntype = "Submachine Gun";
                break;
            case SHOTGUN:
                guntype = "Shotgun";
                break;
            case HAND_GUN:
                guntype = "Hand Gun";
                break;
            case ORIGIN:
                guntype = getDisplayName();
                break;
        }
        lore.add("");
        lore.add(ChatColor.GRAY + "?????????: " + ChatColor.GOLD + getAttackDamage()
                + ChatColor.GRAY + "(HS: " + ChatColor.GOLD + "+" + getHeadShotDamage() + ChatColor.GRAY + ", " +
                "AIM: " + ChatColor.GOLD + "+" + getDamageAimed() + ")");
        lore.add(ChatColor.GRAY + "??????: " + ChatColor.GOLD + getRange());
        lore.add(ChatColor.GRAY + "??????: " + ChatColor.GOLD + getFire());
        lore.add(ChatColor.GRAY + "?????? (???????????????): " + ChatColor.GOLD + getAccuracy()
                + ChatColor.GRAY + "(" + ChatColor.GOLD + getAccuracyOnSneak() + ChatColor.GRAY + ")");
        lore.add(ChatColor.GRAY + "??????????????????: " + ChatColor.GOLD + getReload());
        lore.add(ChatColor.GRAY + "??????????????????: " + ChatColor.GOLD + getKnockBack());
        lore.add(ChatColor.GRAY + "?????? (???????????????): " + ChatColor.GOLD + getRecoil()
                + ChatColor.GRAY + "(" + ChatColor.GOLD + getRecoilOnSneak() + ChatColor.GRAY + ")");
        lore.add(ChatColor.GRAY + "?????????: " + ChatColor.GOLD + getAmmo());
        lore.add("");
        lore.add(ChatColor.YELLOW + guntype);
        lore.add(ChatColor.DARK_GRAY + getId());
        meta.setLore(lore);
        getItem().setItemMeta(meta);
    }
}
