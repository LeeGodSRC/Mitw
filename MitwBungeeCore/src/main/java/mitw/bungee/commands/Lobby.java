package mitw.bungee.commands;

import mitw.bungee.Mitw;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Lobby extends Command {

	public Lobby(final Mitw main) {
		super("lobby", null, "hub");
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {

		final ProxiedPlayer player = (ProxiedPlayer) sender;
		final String serverName = player.getServer().getInfo().getName();

		String toConnect = "waiting";
		if (serverName.equals("CastleWars")) {
			toConnect = "duel";
		}
		player.connect(ProxyServer.getInstance().getServerInfo(toConnect));
		player.sendMessage(Mitw.Prefix + "你返回了大廳");
		return;
	}
}
