package mitw.bungee.commands;

import mitw.bungee.Mitw;
import mitw.bungee.managers.YamlManagers;
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
		YamlManagers.motd = YamlManagers.General.getStringList("motd");
		YamlManagers.General.set("motd", YamlManagers.motd);
		YamlManagers.saveConfig();
		sender.sendMessage(ChatColor.GREEN + "成功重新載入motd");
	}
}
