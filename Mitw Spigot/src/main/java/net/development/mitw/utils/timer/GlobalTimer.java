package net.development.mitw.utils.timer;

import net.development.mitw.utils.timer.events.TimerExtendEvent;
import net.development.mitw.utils.timer.events.TimerPauseEvent;
import net.development.mitw.utils.timer.events.TimerStartEvent;

public abstract class GlobalTimer extends Timer {
	private TimerCooldown runnable;


	public GlobalTimer(final String name, final boolean async, final long defaultCooldown) {

		super(name, async, defaultCooldown);

	}


	public boolean clearCooldown() {

		if (this.runnable != null) {

			this.runnable.cancel();

			this.runnable = null;

			return true;

		}

		return false;

	}


	public boolean isPaused() {

		return (this.runnable != null) && (this.runnable.isPaused());

	}


	public void setPaused(final boolean paused) {

		if ((this.runnable != null) && (this.runnable.isPaused() != paused)) {

			final TimerPauseEvent event = new TimerPauseEvent(this, paused);

			org.bukkit.Bukkit.getPluginManager().callEvent(event);

			if (!event.isCancelled()) {

				this.runnable.setPaused(paused);

			}

		}

	}


	public long getRemaining() {

		return this.runnable == null ? 0L : this.runnable.getRemaining();

	}


	public boolean setRemaining() {

		return setRemaining(this.defaultCooldown, false);

	}


	public boolean setRemaining(final long duration, final boolean overwrite) {

		boolean hadCooldown = false;

		if (this.runnable != null) {

			if (!overwrite)
				return false;

			final TimerExtendEvent event = new TimerExtendEvent(this, this.runnable.getRemaining(), duration);

			org.bukkit.Bukkit.getPluginManager().callEvent(event);

			if (event.isCancelled())
				return false;

			hadCooldown = this.runnable.getRemaining() > 0L;

			this.runnable.setRemaining(duration);

		} else {

			org.bukkit.Bukkit.getPluginManager().callEvent(new TimerStartEvent(this, duration));

			this.runnable = new TimerCooldown(this, duration);

		}

		return !hadCooldown;

	}

}