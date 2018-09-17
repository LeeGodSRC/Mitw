package mitw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import mitw.commands.AC;
import mitw.commands.Broadcast;
import mitw.commands.Ignore;
import mitw.commands.Lobby;
import mitw.commands.Message;
import mitw.commands.Reply;
import mitw.commands.Report;
import mitw.commands.Server;
import mitw.managers.BungeeListener;
import mitw.managers.YamlManagers;
import mitw.modules.MotdDisplay;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Bungee extends Plugin{
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
		getProxy().getConsole().sendMessage(Prefix + "§eLoading MitwCore - Bungee..");
		final PluginManager m = ProxyServer.getInstance().getPluginManager();
		m.registerListener(this, new MotdDisplay(this));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new Lobby(this));
		m.registerCommand(this, new Server(this));
		m.registerCommand(this, new Message());
		m.registerCommand(this, new Reply());
		m.registerCommand(this, new Report());
		m.registerCommand(this, new AC());
		m.registerCommand(this, new Ignore());
		m.registerCommand(this, new Broadcast());
		m.registerListener(this, new BungeeListener());
		final YamlManagers YamlManagers = new YamlManagers(this);
		YamlManagers.SetUp();
		getProxy().getConsole().sendMessage(Prefix + "§eLoad MitwCore - Bungee successfully.");
	}

	@Override
	public void onDisable() {
		this.getLogger().info("Closing " + this.getDescription().getName() + " V" + this.getDescription().getVersion() + " by "
				+ this.getDescription().getAuthor());
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
}
