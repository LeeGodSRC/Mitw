package mitw.bungee.commands;

import mitw.bungee.Mitw;
import mitw.bungee.config.impl.General;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Reload extends Command {

	public Reload(Mitw main) {
		super("reloadmotd");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("mitw.admin")) {
			return;
		}
		General.motd = General.getGeneral().getStringList("motd");
		General.getGeneral().set("motd", General.motd);
		sender.sendMessage(ChatColor.GREEN + "成功重新載入motd");
	}
}
