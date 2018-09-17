package mitw.commands;

import mitw.Bungee;
import mitw.managers.YamlManagers;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Reply extends Command {

	public Reply() {
		super("r", null, "reply");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer target = null;
		if (args.length > 0) {
			if (sender instanceof ProxiedPlayer) {
				final ProxiedPlayer p = (ProxiedPlayer) sender;
				if (Bungee.replys.containsKey(p.getUniqueId())) {
					target = ProxyServer.getInstance().getPlayer(Bungee.replys.get(p.getUniqueId()));
				}
				if (target == null) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&l私訊系統&8]&c 你並沒有可回復的對象!"));
					return;
				}
				if (YamlManagers.Ignores.contains(target.getUniqueId()) && !p.hasPermission("mitw.admin")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + target.getName() + "目前不接受任何人的訊息"));
					return;
				}
				final StringBuilder sb = new StringBuilder("");
				for (int i = 0; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				final String msg = sb.toString();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8(&7私訊給&e " + target.getName() + "&7: &f" + msg + " &7)"));
				target.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8(&7來自&e " + p.getName() + "&7 的私訊:&f " + msg + " &7)"));

			}
		}

	}


}
