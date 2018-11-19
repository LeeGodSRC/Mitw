package net.development.mitw.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PlayerInfo {

	private final UUID uuid;
	@Setter
	private String name;

	public PlayerInfo(final Player player) {
		this(player.getUniqueId(), player.getName());
	}

	public PlayerInfo(final UUID uuid, final String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public Player toBukkitPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public int getPing() {
		final Player bukkitPlayer = this.toBukkitPlayer();

		if (bukkitPlayer == null)
			return -1;
		else
			return bukkitPlayer.spigot().getPing();
	}

}
