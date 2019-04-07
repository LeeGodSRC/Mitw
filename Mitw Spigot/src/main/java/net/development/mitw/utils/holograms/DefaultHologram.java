
package net.development.mitw.utils.holograms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.mitw.Mitw;
import net.development.mitw.utils.holograms.touch.TouchHandler;
import net.development.mitw.utils.holograms.view.ViewHandler;

public class DefaultHologram extends CraftHologram {
	private final List<TouchHandler> touchHandlers = new ArrayList<>();
	private final List<ViewHandler> viewHandlers = new ArrayList<>();
	@Getter
	private final List<UUID> rendered = new ArrayList<>();
	private Location location;
	private String text;
	private boolean touchable;
	private boolean spawned;
	private boolean isAttached;
	private Entity attachedTo;

	private Hologram lineBelow;
	private Hologram lineAbove;
	private BukkitRunnable updater;

	protected DefaultHologram(@Nonnull final Location loc, final String text) {

		this.location = loc;

		this.text = text;

	}

	@Override
	public boolean isSpawned() {

		return this.spawned;

	}

	@Override
	public void spawn(final long ticks) {

		if (ticks < 1L)
			throw new IllegalArgumentException("ticks must be at least 1");

		spawn();

		new BukkitRunnable() {

			@Override
			public void run() {

				DefaultHologram.this.despawn();
			}
		}.runTaskLater(Mitw.getInstance(), ticks);

	}

	@Override
	public boolean spawn() {

		validateDespawned();

		if (!this.packetsBuilt) {

			try {

				buildPackets(false);

				this.packetsBuilt = true;

			} catch (final Exception e) {

				e.printStackTrace();

			}

		}

		try {

			this.spawned = HologramAPI.spawn(this, getLocation().getWorld().getPlayers());

		} catch (final Exception e) {

			e.printStackTrace();

		}

		return this.spawned;

	}

	@Override
	public boolean despawn() {

		validateSpawned();

		try {

			this.spawned = (!HologramAPI.despawn(this, getLocation().getWorld().getPlayers()));

		} catch (final Exception e) {

			e.printStackTrace();

		}

		return !this.spawned;

	}

	@Override
	public Location getLocation() {

		return this.location.clone();

	}

	@Override
	public void setLocation(final Location loc) {

		move(loc);

	}

	@Override
	public String getText() {

		return this.text;

	}

	@Override
	public void setText(final String text) {

		this.text = text;

		if (isSpawned()) {

			try {

				buildPackets(true);

			} catch (final Exception e) {

				e.printStackTrace();

			}

			sendNamePackets(getLocation().getWorld().getPlayers());

		}

	}

	@Override
	public void update() {

		setText(getText());

	}

	@Override
	public void update(final long interval) {

		if (interval == -1L) {

			if (this.updater == null)
				throw new IllegalStateException("Not updating");

			this.updater.cancel();

			this.updater = null;

			return;

		}

		if (this.updater != null)
			throw new IllegalStateException("Already updating");

		if (interval < 1L)
			throw new IllegalArgumentException("Interval must be at least 1");

		this.updater = new BukkitRunnable() {

			@Override
			public void run() {

				DefaultHologram.this.update();

			}

		};

		this.updater.runTaskTimer(Mitw.getInstance(), interval, interval);

	}

	@Override
	public void move(@Nonnull final Location loc) {

		if (loc == null)
			throw new IllegalArgumentException("location cannot be null");

		if (this.location.equals(loc))
			return;

		if (!this.location.getWorld().equals(loc.getWorld()))
			throw new IllegalArgumentException("cannot move to different world");

		this.location = loc;

		if (isSpawned()) {

			try {

				buildPackets(true);

			} catch (final Exception e) {

				e.printStackTrace();

			}

			sendTeleportPackets(getLocation().getWorld().getPlayers(), true, true);

		}

	}

	@Override
	public boolean isTouchable() {

		return (this.touchable) && (HologramAPI.packetsEnabled);

	}

	@Override
	public void setTouchable(final boolean flag) {

		validateTouchEnabled();

		if (flag == isTouchable())
			return;

		this.touchable = flag;

		if (isSpawned()) {

			try {

				buildPackets(true);

			} catch (final Exception e) {

				e.printStackTrace();

			}

			sendSpawnPackets(getLocation().getWorld().getPlayers(), false, true);

		}

	}

	@Override
	public void addTouchHandler(final TouchHandler handler) {

		validateTouchEnabled();

		if (!isTouchable())
			throw new IllegalStateException("Hologram is not touchable");

		this.touchHandlers.add(handler);

	}

	@Override
	public void removeTouchHandler(final TouchHandler handler) {

		validateTouchEnabled();

		if (!isTouchable())
			throw new IllegalStateException("Hologram is not touchable");

		this.touchHandlers.remove(handler);

	}

	@Override
	public Collection<TouchHandler> getTouchHandlers() {

		return new ArrayList<>(this.touchHandlers);

	}

	@Override
	public void clearTouchHandlers() {

		for (final TouchHandler handler : getTouchHandlers()) {

			removeTouchHandler(handler);

		}

	}

	@Override
	public void addViewHandler(final ViewHandler handler) {

		validateViewsEnabled();

		this.viewHandlers.add(handler);

	}

	@Override
	public void removeViewHandler(final ViewHandler handler) {

		validateViewsEnabled();

		this.viewHandlers.remove(handler);

	}

	@Override
	public Collection<ViewHandler> getViewHandlers() {

		return new ArrayList<>(this.viewHandlers);

	}

	@Override
	public void clearViewHandlers() {

		for (final ViewHandler handler : getViewHandlers()) {

			removeViewHandler(handler);

		}

	}

	@Override
	public Hologram addLineBelow(final String text) {

		validateSpawned();

		final Hologram hologram = HologramAPI.createHologram(getLocation().subtract(0.0D, 0.25D, 0.0D), text);

		this.lineBelow = hologram;

		((DefaultHologram) hologram).lineAbove = this;

		hologram.spawn();

		return hologram;

	}

	@Override
	public Hologram getLineBelow() {

		validateSpawned();

		return this.lineBelow;

	}

	@Override
	public boolean removeLineBelow() {

		if (getLineBelow() != null) {

			if (getLineBelow().isSpawned()) {

				getLineBelow().despawn();

			}

			this.lineBelow = null;

			return true;

		}

		return false;

	}

	@Override
	public Collection<Hologram> getLinesBelow() {

		final List<Hologram> list = new ArrayList<>();

		Hologram current = this;

		while ((current = ((DefaultHologram) current).lineBelow) != null) {

			list.add(current);

		}

		return list;

	}

	@Override
	public Hologram addLineAbove(final String text) {

		validateSpawned();

		final Hologram hologram = HologramAPI.createHologram(getLocation().add(0.0D, 0.25D, 0.0D), text);

		this.lineAbove = hologram;

		((DefaultHologram) hologram).lineBelow = this;

		hologram.spawn();

		return hologram;

	}

	@Override
	public Hologram getLineAbove() {

		validateSpawned();

		return this.lineAbove;

	}

	@Override
	public boolean removeLineAbove() {

		if (getLineAbove() != null) {

			if (getLineAbove().isSpawned()) {

				getLineAbove().despawn();

			}

			this.lineAbove = null;

			return true;

		}

		return false;

	}

	@Override
	public Collection<Hologram> getLinesAbove() {

		final List<Hologram> list = new ArrayList<>();

		Hologram current = this;

		while ((current = ((DefaultHologram) current).lineAbove) != null) {

			list.add(current);

		}

		return list;

	}

	@Override
	public Collection<Hologram> getLines() {

		final List<Hologram> list = new ArrayList<>();

		list.addAll(getLinesAbove());

		list.add(this);

		list.addAll(getLinesBelow());

		return list;

	}

	@Override
	public Entity getAttachedTo() {

		return this.attachedTo;

	}

	@Override
	public void setAttachedTo(final Entity attachedTo) {

		if (attachedTo == this.attachedTo)
			return;

		setAttached(attachedTo != null);

		if (attachedTo != null) {

			this.attachedTo = attachedTo;

		}

		if (isSpawned()) {

			try {

				buildPackets(true);

			} catch (final Exception e) {

				e.printStackTrace();

			}

			sendAttachPacket(getLocation().getWorld().getPlayers());

		}

		this.attachedTo = attachedTo;

	}

	public boolean isAttached() {

		return this.isAttached;

	}

	public void setAttached(final boolean isAttached) {

		this.isAttached = isAttached;

	}

	private void validateTouchEnabled() {
	}

	private void validateViewsEnabled() {
	}

	private void validateSpawned() {

		if (!this.spawned)
			throw new IllegalStateException("Not spawned");

	}

	private void validateDespawned() {

		if (this.spawned)
			throw new IllegalStateException("Already spawned");

	}

	@Override
	public int hashCode() {

		int result = super.hashCode();

		result = 31 * result + (this.location == null ? 0 : this.location.hashCode());

		result = 31 * result + (this.spawned ? 1231 : 1237);

		result = 31 * result + (this.text == null ? 0 : this.text.hashCode());

		result = 31 * result + (this.touchHandlers == null ? 0 : this.touchHandlers.hashCode());

		result = 31 * result + (this.touchable ? 1231 : 1237);

		return result;

	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj)
			return true;

		if (!super.equals(obj))
			return false;

		if (getClass() != obj.getClass())
			return false;

		final DefaultHologram other = (DefaultHologram) obj;

		if (this.location == null) {

			if (other.location != null)
				return false;

		} else if (!this.location.equals(other.location))
			return false;

		if (this.spawned != other.spawned)
			return false;

		if (this.text == null) {

			if (other.text != null)
				return false;

		} else if (!this.text.equals(other.text))
			return false;

		if (this.touchHandlers == null) {

			if (other.touchHandlers != null)
				return false;

		} else if (!this.touchHandlers.equals(other.touchHandlers))
			return false;

		return this.touchable == other.touchable;

	}

	@Override
	public String toString() {

		return "{\"location\":\"" + this.location + "\",\"text\":\"" + this.text + "\",\"touchable\":\"" + this.touchable + "\",\"spawned\":\""
				+ this.spawned + "\",\"touchHandlers\":\"" + this.touchHandlers + "\"}";

	}

}
