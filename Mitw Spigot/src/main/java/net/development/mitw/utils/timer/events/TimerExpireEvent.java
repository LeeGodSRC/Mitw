package net.development.mitw.utils.timer.events;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.development.mitw.utils.timer.Timer;

public class TimerExpireEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private final Optional<UUID> userUUID;
	private final Timer timer;

	private Optional<Player> player;

	public TimerExpireEvent(final Timer timer) {
		this.userUUID = Optional.empty();
		this.timer = timer;
	}

	public TimerExpireEvent(final UUID userUUID, final Timer timer) {
		this.userUUID = Optional.ofNullable(userUUID);
		this.timer = timer;
	}

	public TimerExpireEvent(final Player player, final Timer timer) {
		Objects.requireNonNull(player);

		this.player = Optional.of(player);
		this.userUUID = Optional.of(player.getUniqueId());
		this.timer = timer;
	}

	public static HandlerList getHandlerList() {
		return TimerExpireEvent.HANDLERS;
	}

	public Optional<Player> getPlayer() {
		if (player == null) {
			player = this.userUUID.isPresent() ?
					Optional.of(Bukkit.getPlayer(this.userUUID.get())) : Optional.empty();
		}
		return player;
	}

	public Optional<UUID> getUserUUID() {
		return this.userUUID;
	}

	public Timer getTimer() {
		return this.timer;
	}

	@Override
	public HandlerList getHandlers() {
		return TimerExpireEvent.HANDLERS;
	}
}
