package net.development.mitw;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import me.skymc.taboolib.mysql.builder.hikari.HikariHandler;
import net.development.mitw.chat.ChatHandler;
import net.development.mitw.chat.ChatManager;
import net.development.mitw.config.AntiCrash;
import net.development.mitw.config.EzProtector;
import net.development.mitw.config.MySQL;
import net.development.mitw.helpmessage.HelpCommand;
import net.development.mitw.helpmessage.HelpHandler;
import net.development.mitw.hooks.LuckPerms;
import net.development.mitw.language.LanguageAPI;
import net.development.mitw.language.LanguageAPI.LangType;
import net.development.mitw.language.LanguageData;
import net.development.mitw.language.LanguageSQLConnection;
import net.development.mitw.language.impl.LanguageMessages;
import net.development.mitw.listener.ChatListener;
import net.development.mitw.packetlistener.PacketHandler;
import net.development.mitw.security.anticrash.BlockCrashHandler;
import net.development.mitw.security.protector.MitwProtector;
import net.development.mitw.utils.reflection.ReflectionUtils;
import net.minecraft.server.v1_8_R3.BlockPosition;
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

		new BlockCrashHandler();
		chatManager = new ChatManager(this);
		chatHandlers = new HashSet<>();
		helpHandlers = new HashSet<>();

		/** 基礎 Help 訊息 **/
		helpHandlers.add(() -> Arrays.asList(
				"Help1",
				"Help2"));

		HikariHandler.init();
		LuckPerms.hook();

		final LanguageSQLConnection languageSQLConnection = new LanguageSQLConnection(MySQL.LANGUAGE_DATABASE);
		languageData = new LanguageData(this, languageSQLConnection);
		coreLanguage = new LanguageAPI(LangType.CLASS, this, languageData, new LanguageMessages());

		registerListeners();
		registerCommands();

		new MitwProtector().onEnable();

		new BukkitRunnable() {

			@Override
			public void run() {
				final BlockPosition position = new BlockPosition(0, 0, 0);
				position.getX();

				ReflectionUtils.callMethod(ReflectionUtils.makeMethod(position.getClass(), "getX"), position);
			}
		}.runTaskLater(instance, 100L);
	}

	public void registerCommands() {
		Arrays.asList(new HelpCommand()).forEach(command -> registerCommand(command, getName()));
	}

	public void registerListeners() {
		Arrays.asList(new ChatListener(this)).forEach(listener -> getServer().getPluginManager().registerEvents(listener, instance));
	}

	public void addChatHandler(ChatHandler chatHandler) {
		chatHandlers.add(chatHandler);
	}

	public void removeChatHandler(ChatHandler chatHandler) {
		chatHandlers.remove(chatHandler);
	}

	public void addHelpHandler(HelpHandler helpHandler) {
		helpHandlers.add(helpHandler);
	}

	public void removeHelpHandler(HelpHandler helpHandler) {
		helpHandlers.remove(helpHandler);
	}

	public void registerCommand(Command cmd, String fallbackPrefix) {
		MinecraftServer.getServer().server.getCommandMap().register(cmd.getName(), fallbackPrefix, cmd);
	}

}
