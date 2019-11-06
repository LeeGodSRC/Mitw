package net.development.mitw;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import net.development.mitw.config.*;
import net.development.mitw.depend.iSelectorDepend;
import net.development.mitw.jedis.JedisSettings;
import net.development.mitw.jedis.MitwJedis;
import net.development.mitw.jedis.server.KeepAliveHandler;
import net.development.mitw.language.ILanguageData;
import net.development.mitw.queue.module.QueueManager;
import net.development.mitw.reboost.ReboostTask;
import net.development.mitw.tablist.TablistManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.skymc.taboolib.mysql.builder.hikari.HikariHandler;
import net.development.mitw.chat.ChatHandler;
import net.development.mitw.chat.ChatManager;
import net.development.mitw.chat.UpdateDataTask;
import net.development.mitw.commands.CommandHandler;
import net.development.mitw.helpmessage.HelpHandler;
import net.development.mitw.hooks.LuckPerms;
import net.development.mitw.language.LanguageAPI;
import net.development.mitw.language.LanguageAPI.LangType;
import net.development.mitw.language.types.SQLLanguageData;
import net.development.mitw.player.database.PlayerDatabase;
import net.development.mitw.language.impl.LanguageMessages;
import net.development.mitw.listener.ChatListener;
import net.development.mitw.listener.JoinAndQuitListener;
import net.development.mitw.menu.ButtonListener;
import net.development.mitw.namemc.NameMC;
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

	private Gson gson = new Gson();

	private ChatManager chatManager;
	private TablistManager tablistManager;
	private ILanguageData languageData;
	private TimerManager timerManager;
	private NameMC nameMC;
	private LanguageAPI coreLanguage;
	private MitwJedis mitwJedis;
	private KeepAliveHandler keepAliveHandler;
	private PlayerDatabase playerDatabase;
	private ReboostTask reboostTask;
//	private QueueManager queueManager;
	private Set<ChatHandler> chatHandlers;
	private Set<HelpHandler> helpHandlers;

	@Override
	public void onLoad() {
//		this.queueManager = new QueueManager();
//		this.queueManager.onLoad();
	}

	@Override
	public void onEnable() {
		instance = this;

		MySQL.init();
		AntiCrash.init();
		EzProtector.init();
		Security.init();

		nameMC = new NameMC(this);
		timerManager = new TimerManager(this);
		timerManager.loadTimerData();
		tablistManager = new TablistManager();
		tablistManager.enable(this);
		reboostTask = new ReboostTask();
		chatHandlers = new HashSet<>();
		helpHandlers = new HashSet<>();

		helpHandlers.add(player -> Arrays.asList("Help1", "Help2"));

		HikariHandler.init();
		LuckPerms.hook();

		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		iSelectorDepend.register(this);

		playerDatabase = new PlayerDatabase(MySQL.LANGUAGE_DATABASE);
		playerDatabase.connect();
		languageData = new SQLLanguageData(this, playerDatabase);
		coreLanguage = new LanguageAPI(LangType.CLASS, this, languageData, new LanguageMessages());
		chatManager = new ChatManager(this);

//		this.queueManager.onEnable();

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new MenuUpdateTask(), 20L, 20 * 30L);

		registerListeners();
		registerCommands();

		//Maybe cause lag
//		HologramAPI.tickingRange();

		new UpdateDataTask().runTaskTimerAsynchronously(this, 20 * 60 * 20L, 20 * 60 * 20L);

		this.mitwJedis = new MitwJedis(new JedisSettings(Settings.JEDIS_ADDRESS, Settings.JEDIS_PORT, Settings.JEDIS_PASSWORD));
		this.keepAliveHandler = new KeepAliveHandler();

		this.getServer().getScheduler().runTaskTimerAsynchronously(this, this.keepAliveHandler, 20L, 30 * 20L);

		new MitwProtector().onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();

		timerManager.saveTimerData();
		tablistManager.disable(this);

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
