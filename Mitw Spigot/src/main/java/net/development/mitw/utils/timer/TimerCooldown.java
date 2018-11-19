package net.development.mitw.utils.timer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.development.mitw.utils.AsyncUtils;
import net.development.mitw.utils.timer.events.TimerExpireEvent;

public class TimerCooldown {

	@Getter
	private final Timer timer;
	private final UUID owner;
	private BukkitTask eventNotificationTask;
	@Getter
	private long expiryMillis;

	@Getter
	@Setter(AccessLevel.PROTECTED)
	private long pauseMillis;

	protected TimerCooldown(final Timer timer, final long duration) {
		this.owner = null;
		this.timer = timer;
		this.setRemaining(duration);
	}

	protected TimerCooldown(final Timer timer, final UUID playerUUID, final long duration) {
		this.timer = timer;
		this.owner = playerUUID;
		this.setRemaining(duration);
	}

	public long getRemaining() {
		return this.getRemaining(false);
	}

	protected void setRemaining(final long milliseconds) throws IllegalStateException {
		if (milliseconds <= 0L) {
			this.cancel();
			return;
		}

		final long expiryMillis = System.currentTimeMillis() + milliseconds;
		if (expiryMillis != this.expiryMillis) {
			this.expiryMillis = expiryMillis;

			if (this.eventNotificationTask != null) {
				this.eventNotificationTask.cancel();
			}

			final long ticks = milliseconds / 50L;
			this.eventNotificationTask = AsyncUtils.runTaskLater(() -> {
				if (TimerCooldown.this.timer instanceof PlayerTimer && owner != null) {
					((PlayerTimer) timer).handleExpiry(
							Bukkit.getPlayer(TimerCooldown.this.owner), TimerCooldown.this.owner);
				}

				Bukkit.getPluginManager().callEvent(
						new TimerExpireEvent(TimerCooldown.this.owner, TimerCooldown.this.timer));
			}, ticks, timer.isAsync());
		}
	}

	protected long getRemaining(final boolean ignorePaused) {
		if (!ignorePaused && this.pauseMillis != 0L)
			return this.pauseMillis;
		else
			return this.expiryMillis - System.currentTimeMillis();
	}

	protected boolean isPaused() {
		return this.pauseMillis != 0L;
	}

	public void setPaused(final boolean paused) {
		if (paused != this.isPaused()) {
			if (paused) {
				this.pauseMillis = this.getRemaining(true);
				this.cancel();
			} else {
				this.setRemaining(this.pauseMillis);
				this.pauseMillis = 0L;
			}
		}
	}

	protected void cancel() throws IllegalStateException {
		if (this.eventNotificationTask != null) {
			this.eventNotificationTask.cancel();
			this.eventNotificationTask = null;
		}
	}
}
