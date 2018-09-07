package net.development.mitw.scoreboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Test implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		ScoreboardSign scoreboardSign = new ScoreboardSign(e.getPlayer(), "hi");
		scoreboardSign.setLine(0, "hi");
		scoreboardSign.create();
	}

}
