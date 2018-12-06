package net.development.mitw.utils.timer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.utils.FastUUID;
import net.development.mitw.utils.timer.events.TimerClearEvent;
import net.development.mitw.utils.timer.events.TimerExtendEvent;
import net.development.mitw.utils.timer.events.TimerPauseEvent;
import net.development.mitw.utils.timer.events.TimerStartEvent;

public abstract class PlayerTimer extends Timer {

	private static final String COOLDOWN_PATH = "timer-cooldowns.";
	private static final String PAUSE_PATH = "timer-pauses.";

	@Getter
	@Setter
	private boolean saveToFile = true;

	@Getter
	protected final Map<UUID, TimerCooldown> cooldowns = new ConcurrentHashMap<>();

	public PlayerTimer(final String name, final long defaultCooldown, final boolean async) {
		super(name, async, defaultCooldown);
	}

	protected void handleExpiry(final Player player, final UUID playerUUID) {
		this.cooldowns.remove(playerUUID);
	}

	public TimerCooldown clearCooldown(final UUID uuid) {
		return this.clearCooldown(null, uuid);
	}

	public TimerCooldown clearCooldown(final Player player) {
		Objects.requireNonNull(player);
		return clearCooldown(player, player.getUniqueId());
	}

	public TimerCooldown clearCooldown(final Player player, final UUID playerUUID) {
		final TimerCooldown runnable = this.cooldowns.remove(playerUUID);
		if (runnable != null) {
			runnable.cancel();
			if (player == null) {
				Bukkit.getPluginManager().callEvent(new TimerClearEvent(playerUUID, this));
			} else {
				Bukkit.getPluginManager().callEvent(new TimerClearEvent(player, this));
			}
		}

		return runnable;
	}

	public boolean isPaused(final Player player) {
		return this.isPaused(player.getUniqueId());
	}

	public boolean isPaused(final UUID playerUUID) {
		final TimerCooldown runnable = this.cooldowns.get(playerUUID);
		return runnable != null && runnable.isPaused();
	}

	public void setPaused(final UUID playerUUID, final boolean paused) {
		final TimerCooldown runnable = this.cooldowns.get(playerUUID);
		if (runnable != null && runnable.isPaused() != paused) {
			final TimerPauseEvent event = new TimerPauseEvent(playerUUID, this, paused);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				runnable.setPaused(paused);
			}
		}
	}

	public long getRemaining(final Player player) {
		return this.getRemaining(player.getUniqueId());
	}

	public long getRemaining(final UUID playerUUID) {
		final TimerCooldown runnable = this.cooldowns.get(playerUUID);
		return runnable == null ? 0L : runnable.getRemaining();
	}

	public boolean isCooldown(final Player player) {
		return isCooldown(player.getUniqueId());
	}

	public boolean isCooldown(final UUID uniqueId) {
		return cooldowns.containsKey(uniqueId);
	}

	public boolean setCooldown(final Player player, final UUID playerUUID) {
		return this.setCooldown(player, playerUUID, this.defaultCooldown, false);
	}

	public boolean setCooldown(final Player player, final UUID playerUUID, final long duration,
			final boolean overwrite) {
		return this.setCooldown(player, playerUUID, duration, overwrite, null);
	}

	public boolean setCooldown(final Player player, final UUID playerUUID, final long duration, final boolean overwrite,
			final Predicate<Long> currentCooldownPredicate) {
		TimerCooldown runnable = duration > 0L ? this.cooldowns.get(playerUUID)
				: this.clearCooldown(player, playerUUID);
		if (runnable != null) {
			final long remaining = runnable.getRemaining();
			if (!overwrite && remaining > 0L && duration <= remaining)
				return false;

			final TimerExtendEvent event = new TimerExtendEvent(player, playerUUID, this, remaining, duration);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return false;

			boolean flag = true;
			if (currentCooldownPredicate != null) {
				flag = currentCooldownPredicate.test(remaining);
			}

			if (flag) {
				runnable.setRemaining(duration);
			}

			return flag;
		} else {
			Bukkit.getPluginManager().callEvent(new TimerStartEvent(player, playerUUID, this, duration));
			runnable = new TimerCooldown(this, playerUUID, duration);
		}

		this.cooldowns.put(playerUUID, runnable);
		return true;
	}

	@Override
	public void load(final FileConfiguration config) {
		if (!saveToFile)
			return;
		String path = COOLDOWN_PATH + this.name;
		Object object = config.get(path);
		if (object instanceof MemorySection) {
			final MemorySection section = (MemorySection) object;
			final long millis = System.currentTimeMillis();
			for (final String id : section.getKeys(false)) {
				final long remaining = config.getLong(section.getCurrentPath() + '.' + id) - millis;
				if (remaining > 0L) {
					this.setCooldown(null, FastUUID.parseUUID(id), remaining, true);
				}
			}
		}
		path = PAUSE_PATH + this.name;
		if ((object = config.get(path)) instanceof MemorySection) {
			final MemorySection section = (MemorySection) object;
			for (final String id2 : section.getKeys(false)) {
				final TimerCooldown timerRunnable = this.cooldowns.get(UUID.fromString(id2));
				if (timerRunnable == null) {
					continue;
				}
				timerRunnable.setPauseMillis(config.getLong(path + '.' + id2));
			}
		}
	}

	@Override
	public void save(final FileConfiguration config) {
		if (!saveToFile)
			return;
		final Set<Map.Entry<UUID, TimerCooldown>> entrySet = this.cooldowns.entrySet();
		final Map<String, Long> pauseSavemap = new LinkedHashMap<>(entrySet.size());
		final Map<String, Long> cooldownSavemap = new LinkedHashMap<>(entrySet.size());
		for (final Map.Entry<UUID, TimerCooldown> entry : entrySet) {
			final String id = entry.getKey().toString();
			final TimerCooldown runnable = entry.getValue();
			pauseSavemap.put(id, runnable.getPauseMillis());
			cooldownSavemap.put(id, runnable.getExpiryMillis());
		}
		config.set(PAUSE_PATH + this.name, pauseSavemap);
		config.set(COOLDOWN_PATH + this.name, cooldownSavemap);

	}
}