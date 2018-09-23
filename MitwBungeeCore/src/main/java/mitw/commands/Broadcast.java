package mitw.commands;

import java.util.HashMap;
import java.util.Map;

import mitw.Bungee;
import mitw.util.Common;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Broadcast extends Command {
	private static Map<String, String> broadcasts = new HashMap<>(); // servername,displayname

	public Broadcast() {
		super("broadcast", "mitw.admin", "ba");
		broadcasts.put("meetup", "&e模擬UHC&7(Meetup)");
		broadcasts.put("castlewars", "&c堡壘攻防戰&7(CastleWars)");
		broadcasts.put("event", "&6晉級大賽&7(Events)");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			return;
		}
		final String serverName = args[0].toLowerCase();
		if (serverName.equals("uhc")) {
			UHCAlert();
			return;
		}
		if (serverName.contains("sg")) {
			sgAlert(sender, serverName);
			return;
		}
		if (!broadcasts.containsKey(serverName))
			return;
		final BaseComponent[] text = new ComponentBuilder(Common.colored("&7-> " + broadcasts.get(serverName) + " &f即將開始! &8- "))
				.append(genJSONMsg("/server " + serverName)).create();
		for (final String s : Bungee.servers)
			for (final ProxiedPlayer pl : ProxyServer.getInstance().getServerInfo(s).getPlayers())
				Common.tell(pl, text);
		Common.tell(sender, "&a成功發送公告");
		return;
	}

	public void UHCAlert() {
		final TextComponent clickAble = genJSONMsg("/server uhc");
		for (final ProxiedPlayer p : BungeeCord.getInstance().getPlayers()) {
			Common.tell(p, "&7&m------------------------------------", "&6&lMitw&f&lUHC &c&l開放加入", "&f歡迎各位帶著好朋友們一起來參與這場&ePvP盛宴",
					"&8Are you ready....?", "");
			Common.tell(p, clickAble);
			Common.tell(p, "", "&7&m------------------------------------");
		}
	}

	public void sgAlert(CommandSender sender, String server) {
		if (sender instanceof ProxiedPlayer) {
			final ProxiedPlayer p = (ProxiedPlayer) sender;
			if (!p.getServer().getInfo().getName().toLowerCase().contains("sg")) {
				Common.tell(p, Bungee.Prefix + " &cSG公告只限定在你存在sg分流中才能使用!");
				return;
			}
			server = p.getServer().getInfo().getName();
			Common.tell(sender, "&a成功發送公告");
		}
		final BaseComponent[] text = new ComponentBuilder(Common.colored("&7-> " + "&a飢餓遊戲&7(MCSG)" + " &f即將開始! &8- "))
				.append(genJSONMsg("/server " + server)).create();
		for (final String s : Bungee.servers)
			for (final ProxiedPlayer pl : ProxyServer.getInstance().getServerInfo(s).getPlayers())
				Common.tell(pl, text);
	}

	public TextComponent genJSONMsg(String cmd) {
		final TextComponent msg = new TextComponent("§e§l[點我加入!][Click me to join]");
		msg.setClickEvent(new ClickEvent(Action.RUN_COMMAND, cmd));
		return msg;
	}

}
