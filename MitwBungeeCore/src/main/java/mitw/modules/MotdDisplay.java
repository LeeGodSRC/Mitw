package mitw.modules;

import java.util.ArrayList;
import java.util.List;

import mitw.Bungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MotdDisplay implements Listener {

	Bungee main;

	public MotdDisplay(Bungee main) {
		this.main = main;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@EventHandler
	public void onPing(ProxyPingEvent e) {
		final ServerPing serverPing = e.getResponse();

		final StringBuilder str = new StringBuilder();
		str.append(ChatColor.translateAlternateColorCodes('&', "&6&lMitw&f 伺服器 &7[4.0] &e正式上線!"));
		str.append(ChatColor.RESET + "\n");
		str.append(ChatColor.translateAlternateColorCodes('&', "&e亞洲PvP玩家最好的歸宿 &7- &e追求穩定, 品質"));

		final String response = str.toString();

		serverPing.setDescription(response);
		e.setResponse(serverPing);

		serverPing.getPlayers().setOnline(serverPing.getPlayers().getOnline());

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
