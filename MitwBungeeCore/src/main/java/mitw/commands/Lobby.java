package mitw.commands;

import mitw.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Lobby extends Command {

	public Lobby(Bungee main) {
		super("lobby", null, "hub");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {

		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (args.length == 0) {
			player.connect(ProxyServer.getInstance().getServerInfo("waiting"));
			player.sendMessage(Bungee.Prefix + "你返回了大廳");
			return;
		}
	}
}
