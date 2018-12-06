package mitw.commands;

import com.ilummc.tlib.util.Strings;

import mitw.Bungee;
import mitw.util.Common;
import net.md_5.bungee.BungeeCord;
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

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (args.length == 0)
			return;
		final String serverName = args[0].toLowerCase();
		if (serverName.equals("uhc")) {
			UHCAlert();
			return;
		}
		if (serverName.contains("sg")) {
			sgAlert(sender, serverName);
			return;
		}
		if (serverName.contains("meetup") && isBoolean(args[1])) {
			meetupAlert(sender, serverName, Boolean.parseBoolean(args[1]));
			return;
		}
		for (final String s : Bungee.servers) {
			for (final ProxiedPlayer pl : ProxyServer.getInstance().getServerInfo(s).getPlayers()) {
				Common.tell(pl, new ComponentBuilder(Strings.replaceWithOrder(Bungee.language.translate(pl, "alert"), Bungee.language.translate(pl, serverName.toLowerCase()), ""))
						.append(genJSONMsg("/" + serverName, pl)).create());
			}
		}
		Common.tell(sender, "§a成功發送公告");
		return;
	}

	private void meetupAlert(final CommandSender sender, final String serverName, final boolean team) {
		for (final String s : Bungee.servers) {
			for (final ProxiedPlayer pl : ProxyServer.getInstance().getServerInfo(s).getPlayers()) {
				Common.tell(pl, new ComponentBuilder(Strings.replaceWithOrder(Bungee.language.translate(pl, "alert"), Bungee.language.translate(pl, "meetup"), "§7(" + (team ? "§bTeam" : "§eSolo") + "§7)"))
						.append(genJSONMsg("/meetup " + serverName, pl)).create());
			}
		}
	}

	public void UHCAlert() {
		for (final ProxiedPlayer p : BungeeCord.getInstance().getPlayers()) {
			Common.tell(p, "§7§m------------------------------------");
			for (final String string : Bungee.language.translateArrays(p, "uhc")) {
				Common.tell(p, string);
			}
			Common.tell(p, "§8Are you ready....?", "");
			Common.tell(p, genJSONMsg("/uhc", p));
			Common.tell(p, "", "§7§m------------------------------------");
		}
	}

	public void sgAlert(final CommandSender sender, final String server) {
		for (final String s : Bungee.servers) {
			for (final ProxiedPlayer pl : ProxyServer.getInstance().getServerInfo(s).getPlayers()) {
				Common.tell(pl, new ComponentBuilder(Strings.replaceWithOrder(Bungee.language.translate(pl, "alert"), Bungee.language.translate(pl, "sg"), ""))
						.append(genJSONMsg("/sg " + server, pl)).create());
			}
		}
	}

	public TextComponent genJSONMsg(final String cmd, final ProxiedPlayer player) {
		final TextComponent msg = new TextComponent(Bungee.language.translate(player, "clickjoin"));
		msg.setClickEvent(new ClickEvent(Action.RUN_COMMAND, cmd));
		return msg;
	}

	public boolean isBoolean(final String string) {
		try { Boolean.parseBoolean(string); } catch (final Exception e) { return false; }
		return true;
	}

}
