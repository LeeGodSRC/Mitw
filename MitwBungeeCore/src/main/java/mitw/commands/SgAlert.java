package mitw.commands;

import mitw.Bungee;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SgAlert extends Command {

	public SgAlert() {
		super("sgalert", null, "sgl");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("sgl.use"))
			return;
		String serverName;
		if (sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			serverName = p.getServer().getInfo().getName();
		} else
			serverName = BungeeCord.getInstance().getServerInfo(args[0]).getName();
		TextComponent msg = genJSONMsg(colored("     &f&l>>&6&l點擊我加入&r&l<<"), "/server " + serverName);
		for (String a : Bungee.servers)
			for (ProxiedPlayer pl : ProxyServer.getInstance().getServerInfo(a).getPlayers()) {
				pl.sendMessage(colored("&f&m--------&6&l公告&r&m--------"));
				pl.sendMessage(colored("&6飢餓遊戲&7(MCSG)&6即將開始! "));
				pl.sendMessage(msg);
				pl.sendMessage(colored("&m--------------------"));
			}
		if (sender instanceof ProxiedPlayer)
			sender.sendMessage(colored("&a成功發送公告訊息!"));
		return;
	}  

	public TextComponent genJSONMsg(String s, String cmd) {
		TextComponent msg = new TextComponent(s);
		msg.setClickEvent(new ClickEvent(Action.RUN_COMMAND, cmd));
		return msg;
	}

	public String colored(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
