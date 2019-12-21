package mitw.bungee.modules;

import java.util.ArrayList;
import java.util.List;

import mitw.bungee.Mitw;
import mitw.bungee.config.impl.General;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MotdDisplay implements Listener {

	Mitw main;

	public MotdDisplay(final Mitw main) {
		this.main = main;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@EventHandler
	public void onPing(final ProxyPingEvent e) {
		final ServerPing serverPing = e.getResponse();

		final StringBuilder str = new StringBuilder();
		str.append(ChatColor.translateAlternateColorCodes('&', General.motd.get(0)));
		str.append(ChatColor.RESET + "\n");
		str.append(ChatColor.translateAlternateColorCodes('&', General.motd.get(1)));

		final String response = str.toString();

		serverPing.setDescription(response);
		e.setResponse(serverPing);

		final List<String> lines = new ArrayList();
		lines.add("&7&m----------------------");
		lines.add("&6&lMitw&f&l Network");
		lines.add(" ");
		lines.add("&6&l給予喜愛PvP的你!");
		lines.add("&7&m----------------------");

		final PlayerInfo[] sample = new PlayerInfo[lines.size()];
		for (int i = 0; i < sample.length; i++) {
			sample[i] = new PlayerInfo(ChatColor.translateAlternateColorCodes('&', lines.get(i)), "");
		}
		serverPing.getPlayers().setSample(sample);


	}

}
