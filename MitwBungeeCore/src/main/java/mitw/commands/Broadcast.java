package mitw.commands;

import mitw.Bungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Broadcast extends Command {

	public Broadcast() {
		super("broadcast", "mitw.admin", "ba");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			return;
		}
		String serverName = args[0];
		if (serverName.contains("sg")) {
			for (String a : Bungee.servers)
				for (ProxiedPlayer pl : ProxyServer.getInstance().getServerInfo(a).getPlayers()) {
					pl.sendMessage(new ComponentBuilder("§7遊戲 §b飢餓遊戲§7(MCSG)§7即將開始! §8- ").append(genJSONMsg("/server " + serverName)).create());
				}
			if (sender instanceof ProxiedPlayer)
				sender.sendMessage(colored("&a成功發送公告訊息!"));
			return;
		}
		if (serverName.contains("meetup")) {
			for (String a : Bungee.servers)
				for (ProxiedPlayer pl : ProxyServer.getInstance().getServerInfo(a).getPlayers()) {
					pl.sendMessage(new ComponentBuilder("§7遊戲 §e模擬UHC§7(Meetup)§7即將開始! §8- ").append(genJSONMsg("/server " + serverName)).create());
				}
			if (sender instanceof ProxiedPlayer)
				sender.sendMessage(colored("&a成功發送公告訊息!"));
			return;
		}
		if (serverName.contains("castlewars")) {
			for (String a : Bungee.servers)
				for (ProxiedPlayer pl : ProxyServer.getInstance().getServerInfo(a).getPlayers()) {
					pl.sendMessage(new ComponentBuilder("§7遊戲 §c堡壘攻防戰§7(CastleWars)§7即將開始! §8- ").append(genJSONMsg("/server " + serverName)).create());
				}
			if (sender instanceof ProxiedPlayer)
				sender.sendMessage(colored("&a成功發送公告訊息!"));
			return;
		}
		if (serverName.contains("events")) {
			for (String a : Bungee.servers)
				for (ProxiedPlayer pl : ProxyServer.getInstance().getServerInfo(a).getPlayers())
					pl.sendMessage(new ComponentBuilder("§c§l晉級大賽即將開始 §8- ").append(genJSONMsg("/server " + serverName)).create());
			if (sender instanceof ProxiedPlayer)
				sender.sendMessage(colored("&a成功發送公告訊息!"));
			return;
		}
		sender.sendMessage("§c您所在的伺服器無法進行公告!");
		return;
	}

	public TextComponent genJSONMsg(String cmd) {
		TextComponent msg = new TextComponent("§e§l[點我加入!][Click me to join]");
		msg.setClickEvent(new ClickEvent(Action.RUN_COMMAND, cmd));
		return msg;
	}

	public String colored(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
