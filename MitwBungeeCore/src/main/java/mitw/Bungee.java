package mitw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import mitw.commands.AC;
import mitw.commands.Broadcast;
import mitw.commands.Ignore;
import mitw.commands.Lobby;
import mitw.commands.Message;
import mitw.commands.Reload;
import mitw.commands.Reply;
import mitw.commands.Report;
import mitw.commands.Server;
import mitw.managers.BungeeListener;
import mitw.managers.YamlManagers;
import mitw.modules.MotdDisplay;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Bungee extends Plugin {
	public static Bungee ins;
	public static HashMap<UUID, UUID> replys = new HashMap<>();
	public static String Prefix;
	public static String CPrefix;
	public static ArrayList<String> servers = new ArrayList<>();

	static {
		Prefix = "§7[§6Mitw§7] §f";
		CPrefix = "§7[§6Mitw§f控制台§7] §f";
	}

	@Override
	public void onEnable() {
		ins = this;
		registerSgAlertServer();
		registerCommand(
				new Lobby(this),
				new Server(this),
				new Message(),
				new Reply(),
				new Report(),
				new AC(),
				new Ignore(),
				new Broadcast(),
				new Reload(this));
		registerListener(
				new MotdDisplay(this),
				new BungeeListener());
		final YamlManagers YamlManagers = new YamlManagers(this);
		YamlManagers.SetUp();
	}

	@Override
	public void onDisable() {
		final ArrayList<String> tempUUID = new ArrayList<>();
		for (final UUID u : YamlManagers.Ignores) {
			tempUUID.add(u.toString());
		}
		YamlManagers.General.set("ignore", tempUUID);
		YamlManagers.saveConfig();
	}

	private void registerSgAlertServer() {
		servers.add("waiting");
		servers.add("duel");
		servers.add("ffa");
	}

	private void registerCommand(Command... cmds) {
		final PluginManager pm = ProxyServer.getInstance().getPluginManager();
		for (final Command cmd : cmds) {
			pm.registerCommand(this, cmd);
		}
	}

	private void registerListener(Listener... listeners) {
		final PluginManager pm = ProxyServer.getInstance().getPluginManager();
		for (final Listener l : listeners) {
			pm.registerListener(this, l);
		}
	}
}
