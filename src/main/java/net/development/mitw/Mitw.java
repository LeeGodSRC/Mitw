package net.development.mitw;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.development.mitw.chat.ChatHandler;
import net.development.mitw.chat.ChatManager;
import net.development.mitw.config.ConfigManager;
import net.development.mitw.language.LanguageAPI;
import net.development.mitw.language.LanguageAPI.LangType;
import net.development.mitw.language.LanguageData;
import net.development.mitw.language.LanguageSQLConnection;
import net.development.mitw.language.impl.LanguageMessages;
import net.development.mitw.listener.ChatListener;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class Mitw extends JavaPlugin {
	
	@Getter
	private static Mitw instance;
	
	@Getter
	private static final Random random = new Random();
	
	@Getter
	private ChatManager chatManager;
	@Getter
	private ConfigManager configManager;
	
	@Getter
	private LanguageData languageData;
	
	@Getter 
	private LanguageAPI coreLanguage;
	
	@Getter
	private Set<ChatHandler> chatHandlers;
	
	@Override
	public void onEnable() {
		instance = this;
		
		chatManager = new ChatManager(this);
		configManager = new ConfigManager();
		chatHandlers = new HashSet<>();
		
		LanguageSQLConnection languageSQLConnection = new LanguageSQLConnection(configManager.getMainConfig().getString("database.languagedatabase"));
		languageData = new LanguageData(this, languageSQLConnection);
		coreLanguage = new LanguageAPI(LangType.CLASS, this, languageData, new LanguageMessages());
		
		registerListeners();
		registerCommands();
	}
	
	public void registerCommands() {
//		Arrays.asList().forEach(command -> registerCommand(command, getName()));
	}
	
	public void registerListeners() {
		Arrays.asList(new ChatListener(this))
			.forEach(listener -> getServer().getPluginManager().registerEvents(listener, instance));
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
