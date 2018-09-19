package mitw.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mitw.Bungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Server extends Command {

	public Server(Bungee main) {
		super("server");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {

		if (!sender.hasPermission("mitw.admin"))
			return;
		final ProxiedPlayer player = (ProxiedPlayer) sender;
		if (args.length == 0) {
			player.sendMessage(
					Bungee.Prefix + ChatColor.GRAY + "你現在正處於 " + ChatColor.GOLD + player.getServer().getInfo().getName() + ChatColor.GRAY + " 伺服器");
			return;
		} else if (args.length > 0) {
			if (ProxyServer.getInstance().getServerInfo(args[0]) != null) {
				player.connect(ProxyServer.getInstance().getServerInfo(args[0]));
				player.sendMessage(Bungee.Prefix + ChatColor.GRAY + "你從伺服器 " + ChatColor.GOLD + player.getServer().getInfo().getName()
						+ ChatColor.GRAY + " 傳送到了伺服器 " + ChatColor.GOLD + ProxyServer.getInstance().getServerInfo(args[0]).getName());
			} else {
				player.sendMessage(Bungee.Prefix + ChatColor.GRAY + "伺服器 " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " 並不存在");

				final Map<String, ServerInfo> ServerList = ProxyServer.getInstance().getServers();
				final List<String> ServerNameList = new ArrayList<>();
				for (final Entry<String, ServerInfo> Server : ServerList.entrySet()) {
					ServerNameList.add(Server.getValue().getName());
				}

				player.sendMessage(Bungee.Prefix + ChatColor.GREEN + "現有伺服器: " + ChatColor.GRAY + ServerNameList.toString());
			}
		}
	}
}
