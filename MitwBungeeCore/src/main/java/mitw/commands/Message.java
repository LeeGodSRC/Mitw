package mitw.commands;

import java.util.HashSet;
import java.util.Set;

import mitw.Bungee;
import mitw.managers.YamlManagers;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class Message extends Command implements TabExecutor {

	public Message() {
		super("msg", null, new String[] { "m", "tell", "pm" });
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length >= 2) {
			if (sender instanceof ProxiedPlayer) {
				ProxiedPlayer p = (ProxiedPlayer) sender;
				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
				if (target == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&l私訊系統&7]&c 指定玩家並不存在!"));
					return;
				}
				if (target.equals(p)) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&l私訊系統&7]&c 你不能夠私訊你自己!"));
				}
				if (YamlManagers.Ignores.contains(target.getUniqueId()) && !p.hasPermission("mitw.admin")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + target.getName() + "目前不接受任何人的訊息"));
					return;
				}
				StringBuilder sb = new StringBuilder("");
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				String msg = sb.toString();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8(&7私訊給&e " + target.getName() + "&7: &f" + msg + " &7)"));
				target.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8(&7來自&e " + p.getName() + "&7 的私訊:&f " + msg + " &7)"));
				Bungee.replys.put(p.getUniqueId(), target.getUniqueId());
				Bungee.replys.put(target.getUniqueId(), p.getUniqueId());
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&l私訊系統&7]&f 使用方法: /msg <玩家名稱> <訊息>"));
		}

	}

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		Set<String> matches = new HashSet<>();
		if (args.length == 1) {
			for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
				if (p.getName().toUpperCase().startsWith(args[0].toUpperCase())) {
					matches.add(p.getName());
				}
			}
		} else if (args.length == 0) {
			matches.add("msg");
		}
		return matches;
	}

}