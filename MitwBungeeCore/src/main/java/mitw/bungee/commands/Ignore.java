package mitw.bungee.commands;

import mitw.bungee.Mitw;
import mitw.bungee.config.impl.General;
import mitw.bungee.util.RV;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ignore extends Command {
	public Ignore() {
		super("ignore", (String) null, new String[] { "ignoremessage", "ign" });
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (sender instanceof ProxiedPlayer) {
			final ProxiedPlayer player = (ProxiedPlayer) sender;
			if (args.length == 0) {
				if (Mitw.INSTANCE.getIgnoreManager().isIgnoreAll(player.getUniqueId())) {
					Mitw.INSTANCE.getIgnoreManager().removeIgnore(player.getUniqueId(), "*");
					player.sendMessage(Mitw.INSTANCE.getLanguage().translate(player, "deIgnoredAll"));
				} else {
					Mitw.INSTANCE.getIgnoreManager().addIgnore(player.getUniqueId(), "*");
					player.sendMessage(Mitw.INSTANCE.getLanguage().translate(player, "ignoredAll"));
				}
			} else {
				String target = args[0];
				if (Mitw.INSTANCE.getIgnoreManager().isIgnored(player.getUniqueId(), target)) {
					Mitw.INSTANCE.getIgnoreManager().removeIgnore(player.getUniqueId(), target);
					player.sendMessage(Mitw.INSTANCE.getLanguage().translate(player, "deIgnored", RV.o("{0}", target)));
				} else {
					Mitw.INSTANCE.getIgnoreManager().addIgnore(player.getUniqueId(), target);
					player.sendMessage(Mitw.INSTANCE.getLanguage().translate(player, "ignored", RV.o("{0}", target)));
				}
			}
		}

	}
}
