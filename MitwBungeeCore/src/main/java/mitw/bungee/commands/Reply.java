package mitw.bungee.commands;

import mitw.bungee.Mitw;
import mitw.bungee.config.impl.General;
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
				if (Mitw.replys.containsKey(p.getUniqueId())) {
					target = ProxyServer.getInstance().getPlayer(Mitw.replys.get(p.getUniqueId()));
				}
				if (target == null) {
					sender.sendMessage(Mitw.INSTANCE.getLanguage().translate(p, "noReply"));
					return;
				}
				if (Mitw.INSTANCE.getIgnoreManager().isIgnored(target.getUniqueId(), p.getName()) && !p.hasPermission("mitw.admin")) {
					p.sendMessage(Mitw.INSTANCE.getLanguage().translate(p, "cannot_send"));
					return;
				}
				final StringBuilder sb = new StringBuilder("");
				for (int i = 0; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				final String msg = sb.toString();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8(&b-> &e" + target.getName() + "&7: &f" + msg + " &7)"));
				target.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8(&e" + p.getName() + " &a-> :&f " + msg + " &7)"));

			}
		}

	}


}
