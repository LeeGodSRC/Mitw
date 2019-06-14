package net.development.mitw.utils;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import spg.lgdev.protocol.viaversion.api.ViaVersion;

public class BukkitUtil {

	public static Player getDamager(final EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player)
			return (Player) event.getDamager();
		else if (event.getDamager() instanceof Projectile) {
			if (((Projectile) event.getDamager()).getShooter() instanceof Player)
				return (Player) ((Projectile) event.getDamager()).getShooter();
		}

		return null;
	}

	// Mitw
	public static int getVersion(final Player player) {
		return ViaVersion.getInstance().getPlayerVersion(player);
	}

}
