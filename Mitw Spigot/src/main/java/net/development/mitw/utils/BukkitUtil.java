package net.development.mitw.utils;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;

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
		return ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion();
	}

	public static void registerListenersFromPackage(Plugin plugin, String path) {
		Iterator<Class<?>> classes = ClassUtil.getClassesInPackage(plugin, path).iterator();

		while (classes.hasNext()) {
			Class<?> clazz = classes.next();
			if (clazz.isAssignableFrom(Listener.class)) {
				try {
					Listener listener = (Listener) clazz.getConstructor(plugin.getClass()).newInstance(plugin);
					plugin.getServer().getPluginManager().registerEvents(listener, plugin);
				} catch (NoSuchMethodException ex) {
					try {
						Listener listener = (Listener) clazz.newInstance();
						plugin.getServer().getPluginManager().registerEvents(listener, plugin);
					} catch (Exception ex2) {
						ex2.printStackTrace();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

}
