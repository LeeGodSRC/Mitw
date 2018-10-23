package net.development.mitw.utils.holograms;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class HologramListeners implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTeleport(PlayerTeleportEvent e) {
		final Player p = e.getPlayer();
		final Iterator var3 = HologramAPI.getHolograms().iterator();

		while (var3.hasNext()) {
			final Hologram h = (Hologram) var3.next();
			if (h.isSpawned() && h.getLocation().getWorld().getName().equals(e.getTo().getWorld().getName())) {
				try {
					HologramAPI.spawn(h, new LinkedList<>(Collections.singletonList(p)));
				} catch (final Exception var6) {
					var6.printStackTrace();
				}
			}
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldChange(PlayerChangedWorldEvent e) {
		final Player p = e.getPlayer();
		final Iterator var3 = HologramAPI.getHolograms().iterator();

		while (var3.hasNext()) {
			final Hologram h = (Hologram) var3.next();
			if (h.isSpawned() && h.getLocation().getWorld().getName().equals(p.getWorld().getName())) {
				try {
					HologramAPI.spawn(h, new LinkedList<>(Collections.singletonList(p)));
				} catch (final Exception var6) {
					var6.printStackTrace();
				}
			}
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent e) {
		final Iterator var2 = HologramAPI.getHolograms().iterator();

		while (var2.hasNext()) {
			final Hologram h = (Hologram) var2.next();
			if (h.isSpawned() && h.getLocation().getChunk().equals(e.getChunk())) {
				try {
					HologramAPI.spawn(h, h.getLocation().getWorld().getPlayers());
				} catch (final Exception var5) {
					var5.printStackTrace();
				}
			}
		}

	}
}
