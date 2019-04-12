package net.development.mitw.utils.holograms;

import java.lang.reflect.Method;
import java.util.*;

import javax.annotation.Nonnull;

import net.development.mitw.Mitw;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.development.mitw.utils.reflection.Reflection;
import net.development.mitw.utils.reflection.minecraft.Minecraft;
import net.development.mitw.utils.reflection.minecraft.Minecraft.Version;

public abstract class HologramAPI {

	protected static final List<DefaultHologram> holograms;
	protected static boolean is1_8;
	protected static boolean packetsEnabled;

	static {
		is1_8 = Minecraft.VERSION.newerThan(Version.v1_8_R1);
		packetsEnabled = false;
		holograms = new ArrayList<>();
	}

	public HologramAPI() {
	}

	public static void tickingRange() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(Mitw.getInstance(), () -> {
			for (DefaultHologram hologram : holograms) {
				if (!hologram.isSpawned()) {
					continue;
				}
				for (Player player : hologram.getLocation().getWorld().getPlayers()) {
					if (hologram.getLocation().distance(player.getLocation()) > 60 && hologram.getRendered().contains(player.getUniqueId())) {
						despawn(hologram, Collections.singleton(player));
						hologram.getRendered().remove(player.getUniqueId());
					} else if (hologram.getLocation().distance(player.getLocation()) < 60 && !hologram.getRendered().contains(player.getUniqueId())) {
						spawn(hologram, Collections.singleton(player));
						hologram.getRendered().add(player.getUniqueId());
					}
				}
			}
		}, 30L, 30L);
	}

	public static Hologram createHologram(final Location loc, final String text) {
		final DefaultHologram hologram = new DefaultHologram(loc, text);
		holograms.add(hologram);
		return hologram;
	}

	public static boolean removeHologram(final Location loc, final String text) {
		Hologram toRemove = null;
		final Iterator var3 = holograms.iterator();

		while (var3.hasNext()) {
			final Hologram h = (Hologram) var3.next();
			if (h.getLocation().equals(loc) && h.getText().equals(text)) {
				toRemove = h;
				break;
			}
		}

		return toRemove != null && removeHologram(toRemove);
	}

	public static boolean removeHologram(@Nonnull final Hologram hologram) {
		if (hologram.isSpawned()) {
			hologram.despawn();
		}

		return holograms.remove(hologram);
	}

	public static Collection<Hologram> getHolograms() {
		return new ArrayList<>(holograms);
	}

	@SuppressWarnings("unchecked")
	public static boolean spawn(@Nonnull final Hologram hologram, final Collection receivers) {
		checkReceiverWorld(hologram, receivers);
		if (!receivers.isEmpty()) {
			((CraftHologram) hologram).sendSpawnPackets(receivers, true, true);
			((CraftHologram) hologram).sendTeleportPackets(receivers, true, true);
			((CraftHologram) hologram).sendNamePackets(receivers);
			((CraftHologram) hologram).sendAttachPacket(receivers);
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	protected static boolean despawn(@Nonnull final Hologram hologram, final Collection receivers) {
		if (receivers.isEmpty())
			return false;
		else {
			((CraftHologram) hologram).sendDestroyPackets(receivers);
			return true;
		}
	}

	protected static void sendPacket(final Player p, final Object packet) {
		if (p != null && packet != null) {
			try {
				final Object e = Reflection.getHandle(p);
				assert e != null;
				final Object connection = Reflection.getFieldWithException(e.getClass(), "playerConnection").get(e);
				assert connection != null;
				Objects.requireNonNull(Reflection.getMethod(connection.getClass(), "sendPacket", Reflection.getNMSClass("Packet"))).invoke(connection,
						packet);
			} catch (final Exception var4) {}

		} else
			throw new IllegalArgumentException("player and packet cannot be null");
	}

	protected static Collection checkReceiverWorld(final Hologram hologram, final Collection receivers) {
		final Iterator iterator = receivers.iterator();

		while (iterator.hasNext()) {
			final Player next = (Player) iterator.next();
			if (!next.getWorld().equals(hologram.getLocation().getWorld())) {
				iterator.remove();
			}
		}

		return receivers;
	}

	public static boolean is1_8() {
		return is1_8;
	}

	public static boolean packetsEnabled() {
		return packetsEnabled;
	}
}
