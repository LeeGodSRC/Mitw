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
import net.development.mitw.config.EzProtector;
import net.development.mitw.config.MySQL;
import net.development.mitw.hooks.LuckPerms;
import net.development.mitw.language.LanguageAPI;
import net.development.mitw.language.LanguageAPI.LangType;
import net.development.mitw.language.LanguageData;
import net.development.mitw.language.LanguageSQLConnection;
import net.development.mitw.language.impl.LanguageMessages;
import net.development.mitw.listener.ChatListener;
import net.development.mitw.protector.MitwProtector;
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

	@Override
	public void onEnable() {
		instance = this;

		MySQL.init();
		chatManager = new ChatManager(this);
		chatHandlers = new HashSet<>();

		HikariHandler.init();
		LuckPerms.hook();
		EzProtector.init();

		final LanguageSQLConnection languageSQLConnection = new LanguageSQLConnection(MySQL.LANGUAGE_DATABASE);
		languageData = new LanguageData(this, languageSQLConnection);
		coreLanguage = new LanguageAPI(LangType.CLASS, this, languageData, new LanguageMessages());

		registerListeners();
		registerCommands();

		new MitwProtector().onEnable();
	}

	public void registerCommands() {
		//		Arrays.asList().forEach(command -> registerCommand(command, getName()));
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

	public void registerCommand(Command cmd, String fallbackPrefix) {
		MinecraftServer.getServer().server.getCommandMap().register(cmd.getName(), fallbackPrefix, cmd);
	}

}
