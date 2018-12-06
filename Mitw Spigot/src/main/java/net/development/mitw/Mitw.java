package net.development.mitw;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableList;

import lombok.Getter;
import me.skymc.taboolib.mysql.builder.hikari.HikariHandler;
import net.development.mitw.chat.ChatHandler;
import net.development.mitw.chat.ChatManager;
import net.development.mitw.chat.CopyDatatask;
import net.development.mitw.commands.CommandHandler;
import net.development.mitw.config.AntiCrash;
import net.development.mitw.config.EzProtector;
import net.development.mitw.config.MySQL;
import net.development.mitw.config.Security;
import net.development.mitw.helpmessage.HelpHandler;
import net.development.mitw.hooks.LuckPerms;
import net.development.mitw.language.LanguageAPI;
import net.development.mitw.language.LanguageAPI.LangType;
import net.development.mitw.language.LanguageData;
import net.development.mitw.language.LanguageSQLConnection;
import net.development.mitw.language.impl.LanguageMessages;
import net.development.mitw.listener.ChatListener;
import net.development.mitw.listener.JoinAndQuitListener;
import net.development.mitw.menu.ButtonListener;
import net.development.mitw.namemc.NameMC;
import net.development.mitw.packetlistener.PacketHandler;
import net.development.mitw.security.anticrash.BlockCrashHandler;
import net.development.mitw.security.protector.MitwProtector;
import net.development.mitw.task.MenuUpdateTask;
import net.development.mitw.utils.FastRandom;
import net.development.mitw.utils.holograms.Hologram;
import net.development.mitw.utils.holograms.HologramAPI;
import net.development.mitw.utils.holograms.HologramListeners;
import net.development.mitw.utils.timer.TimerManager;
import net.minecraft.server.v1_8_R3.MinecraftServer;

@Getter
public class Mitw extends JavaPlugin {

	@Getter
	private static Mitw instance;

	@Getter
	private static final FastRandom random = new FastRandom();

	private ChatManager chatManager;
	private LanguageData languageData;
	private TimerManager timerManager;
	private NameMC nameMC;
	private LanguageAPI coreLanguage;
	private Set<ChatHandler> chatHandlers;
	private Set<HelpHandler> helpHandlers;

	@Override
	public void onEnable() {
		instance = this;

		new PacketHandler();

		MySQL.init();
		AntiCrash.init();
		EzProtector.init();
		Security.init();

		new BlockCrashHandler();
		nameMC = new NameMC(this);
		timerManager = new TimerManager(this);
		timerManager.loadTimerData();
		chatHandlers = new HashSet<>();
		helpHandlers = new HashSet<>();

		helpHandlers.add(() -> Arrays.asList("Help1", "Help2"));

		HikariHandler.init();
		LuckPerms.hook();

		final LanguageSQLConnection languageSQLConnection = new LanguageSQLConnection(MySQL.LANGUAGE_DATABASE);
		languageData = new LanguageData(this, languageSQLConnection);
		coreLanguage = new LanguageAPI(LangType.CLASS, this, languageData, new LanguageMessages());
		chatManager = new ChatManager(this);

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new MenuUpdateTask(), 20L, 20 * 30L);

		registerListeners();
		registerCommands();


		new CopyDatatask().runTaskTimerAsynchronously(Mitw.getInstance(), 20 * 60 * 20L, 20 * 60 * 20L);

		new MitwProtector().onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();

		timerManager.saveTimerData();

		HologramAPI.getHolograms().forEach(Hologram::despawn);
	}

	public void registerCommands() {

		CommandHandler.init();
		CommandHandler.loadCommandsFromPackage(this, "net.development.mitw.commands.cmd");

	}

	public void registerListeners() {
		Arrays.asList(
				new ChatListener(this),
				new JoinAndQuitListener(),
				new HologramListeners(),
				new ButtonListener())
		.forEach(listener -> getServer().getPluginManager().registerEvents(listener, instance));
	}

	public void addChatHandler(final ChatHandler chatHandler) {
		chatHandlers.add(chatHandler);
	}

	public void removeChatHandler(final ChatHandler chatHandler) {
		chatHandlers.remove(chatHandler);
	}

	public void addHelpHandler(final HelpHandler helpHandler) {
		helpHandlers.add(helpHandler);
	}

	public void removeHelpHandler(final HelpHandler helpHandler) {
		helpHandlers.remove(helpHandler);
	}

	public void registerCommand(final Command cmd, final String fallbackPrefix) {
		MinecraftServer.getServer().server.getCommandMap().register(cmd.getName(), fallbackPrefix, cmd);
	}

	public static void broadcastMessage(final String message) {

		final ImmutableList<Player> players = ImmutableList.copyOf(Mitw.instance.getServer().getOnlinePlayers());
		final int size = players.size();
		final int diff = (int) Math.ceil(players.size() / 20D);

		for (int i = 0, j = 0; i < size; i += diff) {

			if (i >= size)
				return;

			// Some shit for the task
			final int start = i;
			final int end = i + diff;
			Bukkit.getServer().getScheduler().runTaskLater(Mitw.instance, () -> {
				for (int i1 = start; i1 < end; ++i1) {
					// Overshot
					if (i1 >= players.size())
						return;

					players.get(i1).sendMessage(message);
				}
			}, ++j);

		}

	}

}
