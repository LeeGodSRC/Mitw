package net.development.mitw;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

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
import net.development.mitw.packetlistener.Reflection;
import net.development.mitw.security.anticrash.BlockCrashHandler;
import net.development.mitw.security.protector.MitwProtector;
import net.development.mitw.menu.task.MenuUpdateTask;
import net.development.mitw.utils.FastRandom;
import net.development.mitw.utils.holograms.Hologram;
import net.development.mitw.utils.holograms.HologramAPI;
import net.development.mitw.utils.holograms.HologramListeners;
import net.development.mitw.utils.timer.TimerManager;

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

		if (!Reflection.VERSION.contains("7")) {
			new BlockCrashHandler();
		}
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


		new CopyDatatask().runTaskTimerAsynchronously(this, 20 * 60 * 20L, 20 * 60 * 20L);

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
		try {
			final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			final CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			commandMap.register(fallbackPrefix, cmd);
		} catch (final NoSuchFieldException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
