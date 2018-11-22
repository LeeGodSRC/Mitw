package mitw.managers;

import mitw.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandManager {

	public CommandManager(final Bungee plugin) {
		plugin.registerCommand(new Command("uhc") {
			@Override
			public void execute(final CommandSender sender, final String[] args) {
				if (!(sender instanceof ProxiedPlayer))
					return;
				((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo("uhc"));
			}
		}, new Command("meetup") {
			@Override
			public void execute(final CommandSender sender, final String[] args) {
				if (!(sender instanceof ProxiedPlayer))
					return;
				((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo("meetup"));
			}
		}, new Command("sg") {
			@Override
			public void execute(final CommandSender sender, final String[] args) {
				if (!(sender instanceof ProxiedPlayer))
					return;
				if (args.length != 1)
					return;
				if (!args[0].contains("sg-"))
					return;
				((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo(args[0]));
			}
		}, new Command("cw") {
			@Override
			public void execute(final CommandSender sender, final String[] args) {
				if (!(sender instanceof ProxiedPlayer))
					return;
				((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo("cw"));
			}
		}, new Command("events") {
			@Override
			public void execute(final CommandSender sender, final String[] args) {
				if (!(sender instanceof ProxiedPlayer))
					return;
				((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo("events"));
			}
		}, new Command("castlewars") {
			@Override
			public void execute(final CommandSender sender, final String[] args) {
				if (!(sender instanceof ProxiedPlayer))
					return;
				((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo("castlewars"));
			}
		});
	}

}
