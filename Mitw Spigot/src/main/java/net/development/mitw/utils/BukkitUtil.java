package net.development.mitw.utils;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.development.mitw.packetlistener.Reflection;

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

	private interface VersionGetter {
		public int get(Player player);
	}

	private static VersionGetter versionGetter;

	// Mitw
	public static int getVersion(final Player player) {
		return versionGetter.get(player);
	}

	static {
		if (Reflection.VERSION.contains("7")) {
			versionGetter = player -> ((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion();
		} else {
			versionGetter = player -> ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle().playerConnection.networkManager.getProtocolVersion().getProtocol();
		}
	}

}
