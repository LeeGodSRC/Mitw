package net.development.mitw.board;

import net.development.mitw.player.MitwPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public interface FrameAdapter {

	String getTitle(Player player);

	default List<String> getLines(Player player) {
		MitwPlayer mitwPlayer = MitwPlayer.getByUuid(player.getUniqueId());

		if (!mitwPlayer.isLoaded()) {
			return null;
		}

		return this.getLines(player, mitwPlayer);
	}

	List<String> getLines(Player player, MitwPlayer mitwPlayer);

	default long tick() { return 2; }

}
