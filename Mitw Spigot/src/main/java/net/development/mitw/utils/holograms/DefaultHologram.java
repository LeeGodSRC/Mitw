/*     */
package net.development.mitw.utils.holograms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.mitw.Mitw;
import net.development.mitw.utils.holograms.touch.TouchHandler;
import net.development.mitw.utils.holograms.view.ViewHandler;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public class DefaultHologram/*     */ extends CraftHologram
/*     */ {
	/* 53 */ private final List<TouchHandler> touchHandlers = new ArrayList<>();
	/* 54 */ private final List<ViewHandler> viewHandlers = new ArrayList<>();
	/*     */ private Location location;
	/*     */ private String text;
	/*     */ private boolean touchable;
	/*     */ private boolean spawned;
	/*     */ private boolean isAttached;
	/*     */ private Entity attachedTo;
	/*     */
	/*     */ private Hologram lineBelow;
	/*     */ private Hologram lineAbove;
	/*     */ private BukkitRunnable updater;

	/*     */
	/*     */
	protected DefaultHologram(@Nonnull final Location loc, final String text)
	/*     */ {
		/* 62 */
		if (loc == null)
			throw new IllegalArgumentException("location cannot be null");
		/* 63 */
		this.location = loc;
		/* 64 */
		this.text = text;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public boolean isSpawned()
	/*     */ {
		/* 69 */
		return this.spawned;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void spawn(final long ticks)
	/*     */ {
		/* 74 */
		if (ticks < 1L)
			throw new IllegalArgumentException("ticks must be at least 1");
		/* 75 */
		spawn();
		/* 76 */
		new BukkitRunnable()
		/*     */ {
			/*     */
			/*     */
			@Override
			public void run() {
				/* 80 */
				DefaultHologram.this.despawn();
			}
		}
		/*     */
		/* 82 */.runTaskLater(Mitw.getInstance(), ticks);
		/*     */
	}

	/*     */
	/*     */
	@Override
	public boolean spawn()
	/*     */ {
		/* 87 */
		validateDespawned();
		/* 88 */
		if (!this.packetsBuilt) {
			/*     */
			try {
				/* 90 */
				buildPackets(false);
				/* 91 */
				this.packetsBuilt = true;
				/*     */
			} catch (final Exception e) {
				/* 93 */
				e.printStackTrace();
				/*     */
			}
			/*     */
		}
		/*     */
		try {
			/* 97 */
			this.spawned = HologramAPI.spawn(this, getLocation().getWorld().getPlayers());
			/*     */
		} catch (final Exception e) {
			/* 99 */
			e.printStackTrace();
			/*     */
		}
		/* 101 */
		return this.spawned;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public boolean despawn()
	/*     */ {
		/* 106 */
		validateSpawned();
		/*     */
		try {
			/* 108 */
			this.spawned = (!HologramAPI.despawn(this, getLocation().getWorld().getPlayers()));
			/*     */
		} catch (final Exception e) {
			/* 110 */
			e.printStackTrace();
			/*     */
		}
		/* 112 */
		return !this.spawned;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Location getLocation()
	/*     */ {
		/* 122 */
		return this.location.clone();
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void setLocation(final Location loc)
	/*     */ {
		/* 117 */
		move(loc);
		/*     */
	}

	/*     */
	/*     */
	@Override
	public String getText()
	/*     */ {
		/* 140 */
		return this.text;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void setText(final String text)
	/*     */ {
		/* 127 */
		this.text = text;
		/* 128 */
		if (isSpawned()) {
			/*     */
			try {
				/* 130 */
				buildPackets(true);
				/*     */
			} catch (final Exception e) {
				/* 132 */
				e.printStackTrace();
				/*     */
			}
			/* 134 */
			sendNamePackets(getLocation().getWorld().getPlayers());
			/*     */
		}
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void update()
	/*     */ {
		/* 145 */
		setText(getText());
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void update(final long interval)
	/*     */ {
		/* 150 */
		if (interval == -1L) {
			/* 151 */
			if (this.updater == null)
				throw new IllegalStateException("Not updating");
			/* 152 */
			this.updater.cancel();
			/* 153 */
			this.updater = null;
			/* 154 */
			return;
			/*     */
		}
		/* 156 */
		if (this.updater != null)
			throw new IllegalStateException("Already updating");
		/* 157 */
		if (interval < 1L)
			throw new IllegalArgumentException("Interval must be at least 1");
		/* 158 */
		this.updater = new BukkitRunnable()
				/*     */ {
			/*     */
			@Override
			public void run()
			/*     */ {
				/* 162 */
				DefaultHologram.this.update();
				/*     */
			}
			/* 164 */
		};
		/* 165 */
		this.updater.runTaskTimer(Mitw.getInstance(), interval, interval);
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void move(@Nonnull final Location loc)
	/*     */ {
		/* 170 */
		if (loc == null)
			throw new IllegalArgumentException("location cannot be null");
		/* 171 */
		if (this.location.equals(loc))
			return;
		/* 172 */
		if (!this.location.getWorld().equals(loc.getWorld()))
			throw new IllegalArgumentException("cannot move to different world");
		/* 173 */
		this.location = loc;
		/* 174 */
		if (isSpawned()) {
			/*     */
			try {
				/* 176 */
				buildPackets(true);
				/*     */
			} catch (final Exception e) {
				/* 178 */
				e.printStackTrace();
				/*     */
			}
			/* 180 */
			sendTeleportPackets(getLocation().getWorld().getPlayers(), true, true);
			/*     */
		}
		/*     */
	}

	/*     */
	/*     */
	@Override
	public boolean isTouchable()
	/*     */ {
		/* 201 */
		return (this.touchable) && (HologramAPI.packetsEnabled);
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void setTouchable(final boolean flag)
	/*     */ {
		/* 186 */
		validateTouchEnabled();
		/* 187 */
		if (flag == isTouchable())
			return;
		/* 188 */
		this.touchable = flag;
		/* 189 */
		if (isSpawned()) {
			/*     */
			try {
				/* 191 */
				buildPackets(true);
				/*     */
			} catch (final Exception e) {
				/* 193 */
				e.printStackTrace();
				/*     */
			}
			/* 195 */
			sendSpawnPackets(getLocation().getWorld().getPlayers(), false, true);
			/*     */
		}
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void addTouchHandler(final TouchHandler handler)
	/*     */ {
		/* 206 */
		validateTouchEnabled();
		/* 207 */
		if (!isTouchable())
			throw new IllegalStateException("Hologram is not touchable");
		/* 208 */
		this.touchHandlers.add(handler);
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void removeTouchHandler(final TouchHandler handler)
	/*     */ {
		/* 213 */
		validateTouchEnabled();
		/* 214 */
		if (!isTouchable())
			throw new IllegalStateException("Hologram is not touchable");
		/* 215 */
		this.touchHandlers.remove(handler);
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Collection<TouchHandler> getTouchHandlers()
	/*     */ {
		/* 220 */
		return new ArrayList<>(this.touchHandlers);
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void clearTouchHandlers()
	/*     */ {
		/* 225 */
		for (final TouchHandler handler : getTouchHandlers()) {
			/* 226 */
			removeTouchHandler(handler);
			/*     */
		}
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void addViewHandler(final ViewHandler handler)
	/*     */ {
		/* 232 */
		validateViewsEnabled();
		/* 233 */
		this.viewHandlers.add(handler);
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void removeViewHandler(final ViewHandler handler)
	/*     */ {
		/* 238 */
		validateViewsEnabled();
		/* 239 */
		this.viewHandlers.remove(handler);
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Collection<ViewHandler> getViewHandlers()
	/*     */ {
		/* 244 */
		return new ArrayList<>(this.viewHandlers);
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void clearViewHandlers()
	/*     */ {
		/* 249 */
		for (final ViewHandler handler : getViewHandlers()) {
			/* 250 */
			removeViewHandler(handler);
			/*     */
		}
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Hologram addLineBelow(final String text)
	/*     */ {
		/* 256 */
		validateSpawned();
		/* 257 */
		final Hologram hologram = HologramAPI.createHologram(getLocation().subtract(0.0D, 0.25D, 0.0D), text);
		/* 258 */
		this.lineBelow = hologram;
		/* 259 */
		((DefaultHologram) hologram).lineAbove = this;
		/*     */
		/* 261 */
		hologram.spawn();
		/* 262 */
		return hologram;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Hologram getLineBelow()
	/*     */ {
		/* 267 */
		validateSpawned();
		/* 268 */
		return this.lineBelow;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public boolean removeLineBelow()
	/*     */ {
		/* 273 */
		if (getLineBelow() != null) {
			/* 274 */
			if (getLineBelow().isSpawned()) {
				/* 275 */
				getLineBelow().despawn();
				/*     */
			}
			/* 277 */
			this.lineBelow = null;
			/* 278 */
			return true;
			/*     */
		}
		/* 280 */
		return false;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Collection<Hologram> getLinesBelow()
	/*     */ {
		/* 285 */
		final List<Hologram> list = new ArrayList<>();
		/*     */
		/* 287 */
		Hologram current = this;
		/* 288 */
		while ((current = ((DefaultHologram) current).lineBelow) != null) {
			/* 289 */
			list.add(current);
			/*     */
		}
		/*     */
		/* 292 */
		return list;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Hologram addLineAbove(final String text)
	/*     */ {
		/* 297 */
		validateSpawned();
		/* 298 */
		final Hologram hologram = HologramAPI.createHologram(getLocation().add(0.0D, 0.25D, 0.0D), text);
		/* 299 */
		this.lineAbove = hologram;
		/* 300 */
		((DefaultHologram) hologram).lineBelow = this;
		/*     */
		/* 302 */
		hologram.spawn();
		/* 303 */
		return hologram;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Hologram getLineAbove()
	/*     */ {
		/* 308 */
		validateSpawned();
		/* 309 */
		return this.lineAbove;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public boolean removeLineAbove()
	/*     */ {
		/* 314 */
		if (getLineAbove() != null) {
			/* 315 */
			if (getLineAbove().isSpawned()) {
				/* 316 */
				getLineAbove().despawn();
				/*     */
			}
			/* 318 */
			this.lineAbove = null;
			/* 319 */
			return true;
			/*     */
		}
		/* 321 */
		return false;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Collection<Hologram> getLinesAbove()
	/*     */ {
		/* 326 */
		final List<Hologram> list = new ArrayList<>();
		/*     */
		/* 328 */
		Hologram current = this;
		/* 329 */
		while ((current = ((DefaultHologram) current).lineAbove) != null) {
			/* 330 */
			list.add(current);
			/*     */
		}
		/*     */
		/* 333 */
		return list;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Collection<Hologram> getLines()
	/*     */ {
		/* 338 */
		final List<Hologram> list = new ArrayList<>();
		/* 339 */
		list.addAll(getLinesAbove());
		/* 340 */
		list.add(this);
		/* 341 */
		list.addAll(getLinesBelow());
		/* 342 */
		return list;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public Entity getAttachedTo()
	/*     */ {
		/* 366 */
		return this.attachedTo;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public void setAttachedTo(final Entity attachedTo)
	/*     */ {
		/* 347 */
		if (attachedTo == this.attachedTo)
			return;
		/* 348 */
		setAttached(attachedTo != null);
		/* 349 */
		if (attachedTo != null) {
			/* 350 */
			this.attachedTo = attachedTo;
			/*     */
		}
		/* 352 */
		if (isSpawned()) {
			/*     */
			try {
				/* 354 */
				buildPackets(true);
				/*     */
			} catch (final Exception e) {
				/* 356 */
				e.printStackTrace();
				/*     */
			}
			/* 358 */
			sendAttachPacket(getLocation().getWorld().getPlayers());
			/*     */
		}
		/*     */
		/* 361 */
		this.attachedTo = attachedTo;
		/*     */
	}

	/*     */
	/*     */
	public boolean isAttached() {
		/* 370 */
		return this.isAttached;
		/*     */
	}

	/*     */
	/*     */
	public void setAttached(final boolean isAttached) {
		/* 374 */
		this.isAttached = isAttached;
		/*     */
	}

	/*     */
	/*     */
	private void validateTouchEnabled() {
	}

	/*     */
	/*     */
	private void validateViewsEnabled() {
	}

	/*     */
	/*     */
	private void validateSpawned() {
		/* 386 */
		if (!this.spawned)
			throw new IllegalStateException("Not spawned");
		/*     */
	}

	/*     */
	/*     */
	private void validateDespawned() {
		/* 390 */
		if (this.spawned)
			throw new IllegalStateException("Already spawned");
		/*     */
	}

	/*     */
	/*     */
	@Override
	public int hashCode()
	/*     */ {
		/* 396 */
		int result = super.hashCode();
		/* 397 */
		result = 31 * result + (this.location == null ? 0 : this.location.hashCode());
		/* 398 */
		result = 31 * result + (this.spawned ? 1231 : 1237);
		/* 399 */
		result = 31 * result + (this.text == null ? 0 : this.text.hashCode());
		/* 400 */
		result = 31 * result + (this.touchHandlers == null ? 0 : this.touchHandlers.hashCode());
		/* 401 */
		result = 31 * result + (this.touchable ? 1231 : 1237);
		/* 402 */
		return result;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public boolean equals(final Object obj)
	/*     */ {
		/* 407 */
		if (this == obj)
			return true;
		/* 408 */
		if (!super.equals(obj))
			return false;
		/* 409 */
		if (getClass() != obj.getClass())
			return false;
		/* 410 */
		final DefaultHologram other = (DefaultHologram) obj;
		/* 411 */
		if (this.location == null) {
			/* 412 */
			if (other.location != null)
				return false;
			/* 413 */
		} else if (!this.location.equals(other.location))
			return false;
		/* 414 */
		if (this.spawned != other.spawned)
			return false;
		/* 415 */
		if (this.text == null) {
			/* 416 */
			if (other.text != null)
				return false;
			/* 417 */
		} else if (!this.text.equals(other.text))
			return false;
		/* 418 */
		if (this.touchHandlers == null) {
			/* 419 */
			if (other.touchHandlers != null)
				return false;
			/* 420 */
		} else if (!this.touchHandlers.equals(other.touchHandlers))
			return false;
		/* 421 */
		return this.touchable == other.touchable;
		/*     */
	}

	/*     */
	/*     */
	@Override
	public String toString()
	/*     */ {
		/* 427 */
		return "{\"location\":\"" + this.location + "\",\"text\":\"" + this.text + "\",\"touchable\":\""
		+ this.touchable + "\",\"spawned\":\"" + this.spawned + "\",\"touchHandlers\":\"" + this.touchHandlers
		+ "\"}";
		/*     */
	}
	/*     */
}

/*
 * Location:
 * C:\Users\msi\Downloads\HologramAPI_v1.6.2.jar!\de\inventivegames\hologram\
 * DefaultHologram.class Java compiler version: 7 (51.0) JD-Core Version: 0.7.1
 */