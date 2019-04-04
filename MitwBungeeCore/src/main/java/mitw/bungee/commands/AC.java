package mitw.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AC extends Command {
	public AC() {
		super("ac", null, new String[] { "adminchat", "staffchat" });
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if ((sender instanceof ProxiedPlayer)) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if (p.hasPermission("mitw.admin")) {
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED + "請不要使用空白AC");
					for (ProxiedPlayer p1 : ProxyServer.getInstance().getPlayers()) { 
						if (p1.hasPermission("mitw.admin")) {
							p1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l管理員聊天室 - &7("
									+ p.getServer().getInfo().getName() + "&7)&b" + p.getName() + "&9: 我是GAY"));
						}
					} 
					return;
				}
				StringBuilder sb = new StringBuilder("");
				for (int i = 0; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				String msg = sb.toString();
				for (ProxiedPlayer p1 : ProxyServer.getInstance().getPlayers()) {
					if (p1.hasPermission("mitw.admin")) {
						p1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l管理員聊天室 - &7("
								+ p.getServer().getInfo().getName() + "&7)&b" + p.getName() + "&f:&e " + msg));
					}
				} 
			}
		}
	} 
}

