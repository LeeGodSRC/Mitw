package net.development.mitw;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.skymc.taboolib.mysql.builder.hikari.HikariHandler;
import net.development.mitw.chat.ChatHandler;
import net.development.mitw.chat.ChatManager;
import net.development.mitw.chat.CopyDatatask;
import net.development.mitw.command.ChatReportCommand;
import net.development.mitw.command.ToxicCommand;
import net.development.mitw.config.AntiCrash;
import net.development.mitw.config.EzProtector;
import net.development.mitw.config.MySQL;
import net.development.mitw.config.Security;
import net.development.mitw.helpmessage.HelpCommand;
import net.development.mitw.helpmessage.HelpHandler;
import net.development.mitw.hooks.LuckPerms;
import net.development.mitw.language.LanguageAPI;
import net.development.mitw.language.LanguageAPI.LangType;
import net.development.mitw.language.LanguageData;
import net.development.mitw.language.LanguageSQLConnection;
import net.development.mitw.language.impl.LanguageMessages;
import net.development.mitw.listener.ChatListener;
import net.development.mitw.listener.PlayerLoginListener;
import net.development.mitw.namemc.NameMC;
import net.development.mitw.namemc.NameMCVoteCommand;
import net.development.mitw.packetlistener.PacketHandler;
import net.development.mitw.security.anticrash.BlockCrashHandler;
import net.development.mitw.security.protector.MitwProtector;
import net.development.mitw.utils.holograms.Hologram;
import net.development.mitw.utils.holograms.HologramAPI;
import net.development.mitw.utils.holograms.HologramListeners;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class Mitw extends JavaPlugin {

	@Getter
	private static Mitw instance;

	@Getter
	private static final Random random = new Random();

	@Getter
	private ChatManager chatManager;
	@Getter
	private LanguageData languageData;
	@Getter
	private NameMC nameMC;

	@Getter
	private LanguageAPI coreLanguage;

	@Getter
	private Set<ChatHandler> chatHandlers;
	@Getter
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
		chatHandlers = new HashSet<>();
		helpHandlers = new HashSet<>();

		helpHandlers.add(() -> Arrays.asList("Help1", "Help2"));

		HikariHandler.init();
		LuckPerms.hook();

		final LanguageSQLConnection languageSQLConnection = new LanguageSQLConnection(MySQL.LANGUAGE_DATABASE);
		languageData = new LanguageData(this, languageSQLConnection);
		coreLanguage = new LanguageAPI(LangType.CLASS, this, languageData, new LanguageMessages());
		chatManager = new ChatManager(this);

		registerListeners();
		registerCommands();


		new CopyDatatask().runTaskTimerAsynchronously(Mitw.getInstance(), 20*60*20, 20*60*20);

		new MitwProtector().onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();

		HologramAPI.getHolograms().forEach(Hologram::despawn);
	}

	public void registerCommands() {
		Arrays.asList(
				new HelpCommand(),
				new NameMCVoteCommand(),
				new ToxicCommand(),
				new ChatReportCommand()
				).forEach(command -> registerCommand(command, getName()));
	}

	public void registerListeners() {
		Arrays.asList(
				new ChatListener(this),
				new PlayerLoginListener(),
				new HologramListeners())
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

}
