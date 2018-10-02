package mitw.commands;

import mitw.Bungee;
import mitw.managers.YamlManagers;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Reload extends Command {

	public Reload(Bungee main) {
		super("reloadmotd");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("mitw.admin")) {
			return;
		}
		YamlManagers.motd = YamlManagers.General.getStringList("motd");
		YamlManagers.General.set("motd", YamlManagers.motd);
		YamlManagers.saveConfig();
		sender.sendMessage(ChatColor.GREEN + "成功重新載入motd");
	}
}
