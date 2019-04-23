package mitw.bungee.commands;

import java.util.HashSet;
import java.util.Set;

import mitw.bungee.Mitw;
import mitw.bungee.config.impl.General;
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
	public void execute(final CommandSender sender, final String[] args) {
		if (args.length >= 2) {
			if (sender instanceof ProxiedPlayer) {
				final ProxiedPlayer p = (ProxiedPlayer) sender;
				final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
				if (target == null) {
					p.sendMessage(Mitw.INSTANCE.getLanguage().translate(p, "targetNotExists"));
					return;
				}
				if (target.equals(p)) {
					p.sendMessage(Mitw.INSTANCE.getLanguage().translate(p, "cannot_send_self"));
					return;
				}
				if (Mitw.INSTANCE.getIgnoreManager().isIgnored(target.getUniqueId(), p.getName()) && !p.hasPermission("mitw.admin")) {
					p.sendMessage(Mitw.INSTANCE.getLanguage().translate(p, "cannot_send"));
					return;
				}
				final StringBuilder sb = new StringBuilder("");
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				final String msg = sb.toString();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8(&b-> &e" + target.getName() + "&7: &f" + msg + " &7)"));
				target.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8(&e" + p.getName() + " &a->&7:&f " + msg + " &7)"));
				Mitw.replys.put(p.getUniqueId(), target.getUniqueId());
				Mitw.replys.put(target.getUniqueId(), p.getUniqueId());
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/msg <player 玩家名稱> <message 訊息>"));
		}

	}

	@Override
	public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
		final Set<String> matches = new HashSet<>();
		if (args.length == 1) {
			for (final ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
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
