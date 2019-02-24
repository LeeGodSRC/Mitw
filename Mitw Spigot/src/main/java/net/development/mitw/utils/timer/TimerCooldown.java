package net.development.mitw.utils.timer;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.development.mitw.Mitw;
import net.development.mitw.utils.timer.events.TimerExpireEvent;

public class TimerCooldown {

	@Getter
	private final Timer timer;
	private final UUID owner;
	@Getter
	private long expiryMillis;

	@Getter
	@Setter(AccessLevel.PROTECTED)
	private long pauseMillis;

	@Getter
	private boolean cancelled;

	protected TimerCooldown(final Timer timer, final long duration) {
		this.owner = null;
		this.timer = timer;
		this.setRemaining(duration);
		Mitw.getInstance().getTimerManager().getTimerCooldowns().add(this);
	}

	protected TimerCooldown(final Timer timer, final UUID playerUUID, final long duration) {
		this.timer = timer;
		this.owner = playerUUID;
		this.setRemaining(duration);
		Mitw.getInstance().getTimerManager().getTimerCooldowns().add(this);
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
		}
	}

	protected long getRemaining(final boolean ignorePaused) {
		if (!ignorePaused && this.pauseMillis != 0L)
			return this.pauseMillis;
		else
			return this.expiryMillis - System.currentTimeMillis();
	}

	protected long getRemaining(final boolean ignorePaused, final long now) {
		if (!ignorePaused && this.pauseMillis != 0L)
			return this.pauseMillis;
		else
			return this.expiryMillis - now;
	}

	protected boolean isPaused() {
		return this.pauseMillis != 0L;
	}

	public void setPaused(final boolean paused) {
		if (paused != this.isPaused()) {
			if (paused) {
				this.pauseMillis = this.getRemaining(true);
			} else {
				this.setRemaining(this.pauseMillis);
				this.pauseMillis = 0L;
			}
		}
	}

	public boolean check(final long now) {
		if (cancelled)
			return true;
		if (getRemaining(false, now) <= 0) {
			if (TimerCooldown.this.timer instanceof PlayerTimer && owner != null) {
				((PlayerTimer) timer).handleExpiry(
						Mitw.getInstance().getServer().getPlayer(TimerCooldown.this.owner), TimerCooldown.this.owner);
			}
			Mitw.getInstance().getServer().getPluginManager().callEvent(
					new TimerExpireEvent(TimerCooldown.this.owner, TimerCooldown.this.timer));
			return true;
		}
		return false;
	}

	protected void cancel() {
		this.cancelled = true;
	}
}
