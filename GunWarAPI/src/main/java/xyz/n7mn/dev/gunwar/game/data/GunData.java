package xyz.n7mn.dev.gunwar.game.data;

public interface GunData extends ItemData {

    public boolean isReloading();

    public boolean canFire();

    public int getAmmo();

    public void setAmmo(int ammo);

    public void fire();

    public void cancelFireCooldown();

    public void reload();

    public void cancelReload();

}