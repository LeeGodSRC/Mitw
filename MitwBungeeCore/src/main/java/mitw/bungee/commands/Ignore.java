package mitw.bungee.commands;

import mitw.bungee.config.impl.General;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ignore extends Command {
	public Ignore() {
		super("ignore", (String) null, new String[] { "ignoremessage", "ign" });
	}

	@Override
	@SuppressWarnings("deprecation")
	public void execute(final CommandSender sender, final String[] args) {
		if (sender instanceof ProxiedPlayer) {
			final ProxiedPlayer p = (ProxiedPlayer) sender;
			if (General.Ignores.contains(p.getUniqueId())) {
				General.Ignores.remove(p.getUniqueId());
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&l私訊系統&7]&a 從現在開始你將允許他人向您傳送訊息!"));
			} else {
				General.Ignores.add(p.getUniqueId());
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&l私訊系統&7]&c 從現在開始你將不允許他人向您傳送訊息!"));
			}
		}

	}
}