/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.development.mitw.protector;

import static net.development.mitw.protector.utils.MessageUtil.color;
import static net.development.mitw.protector.utils.TabCompletion.registerCompletions;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import net.development.mitw.Mitw;
import net.development.mitw.config.EzProtector;
import net.development.mitw.protector.commands.EZPCommand;
import net.development.mitw.protector.listeners.CommandEventListener;
import net.development.mitw.protector.listeners.PacketEventListener;
import net.development.mitw.protector.listeners.PacketMessageListener;
import net.development.mitw.protector.listeners.PlayerJoinListener;

public class MitwProtector {

	// Variables
	public static final ArrayList<String> plugins = new ArrayList<>();
	public static String ZIG;
	public static String BSM;
	public static String MCBRAND;
	public static String SCHEMATICA;
	public static String player = "";
	public static String playerCommand = "";
	public static String errorMessage = "";
	private static JavaPlugin plugin;
	private static String prefix;

	private final PacketMessageListener pluginMessageListener;

	public MitwProtector() {
		plugin = Mitw.getInstance();
		this.pluginMessageListener = new PacketMessageListener(Mitw.getInstance());
	}

	/**
	 * Gets the plugin variable from the main class.
	 *
	 * @return The plugin variable.
	 */
	public static JavaPlugin getPlugin() {
		return plugin;
	}

	/**
	 * Replaces placeholders with actual information in a given string.
	 *
	 * @param args
	 *            The string that should be filtered.
	 * @return The new string with replaced placeholders.
	 */
	public static String placeholders(String args) {
		return StringEscapeUtils.unescapeJava(args.replace("%prefix%", color(prefix)).replace("%player%", player)
				.replace("%errormessage%", errorMessage).replace("%command%", "/" + playerCommand));
	}

	public void onEnable() {
		prefix = EzProtector.getInstance().getString("prefix");

		ZIG = "5zig_Set";
		BSM = "BSM";
		MCBRAND = "MC|Brand";
		SCHEMATICA = "schematica";

		PacketEventListener.protocolLibHook();

		final Server server = Bukkit.getServer();

		server.getMessenger().registerIncomingPluginChannel(plugin, ZIG, this.pluginMessageListener);
		server.getMessenger().registerIncomingPluginChannel(plugin, BSM, this.pluginMessageListener);
		server.getMessenger().registerIncomingPluginChannel(plugin, MCBRAND, this.pluginMessageListener);
		server.getMessenger().registerIncomingPluginChannel(plugin, SCHEMATICA, this.pluginMessageListener);

		server.getMessenger().registerOutgoingPluginChannel(plugin, ZIG);
		server.getMessenger().registerOutgoingPluginChannel(plugin, BSM);
		server.getMessenger().registerOutgoingPluginChannel(plugin, MCBRAND);
		server.getMessenger().registerOutgoingPluginChannel(plugin, SCHEMATICA);

		plugin.getCommand("ezp").setExecutor(new EZPCommand());

		// Set up 1.13 tab completion
		if (CommodoreProvider.isSupported()) {
			final Commodore commodore = CommodoreProvider.getCommodore(plugin);
			registerCompletions(commodore, plugin.getCommand("ezp"));
		}

		server.getPluginManager().registerEvents(new CommandEventListener(), plugin);
		server.getPluginManager().registerEvents(new PlayerJoinListener(), plugin);

		// Add custom plugin list to the internal ArrayList
		plugins.addAll(Arrays.asList(EzProtector.getInstance().getString("custom-plugins.plugins").split(", ")));

	}
}
