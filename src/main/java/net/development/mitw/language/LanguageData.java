package net.development.mitw.language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;

public class LanguageData implements Listener{

	static final String DEFAULT_LANGUAGE = "zh_tw";
	
	private static Map<UUID, String> playerLangs = new HashMap<>();
	
	@Getter @Setter private Plugin plugin;
	@Getter @Setter private LanguageSQLConnection conn;

	public LanguageData(Plugin plugin, LanguageSQLConnection conn) {
		this.plugin = plugin;
		this.conn = conn;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public String getLang(Player p) {
		if (playerLangs.containsKey(p.getUniqueId())) {
			return playerLangs.get(p.getUniqueId());
		}
		try {
			ResultSet result = conn.getSqlTable().executeSelect("uuid = ?")
	                .dataSource(conn.getDatabase().getDataSource())
	                .statement(s -> s.setString(1, p.getUniqueId().toString()))
	                .result(r -> r)
	                .run(null, null);
			String lang = null;
			if(result.isBeforeFirst()) {
				while(result.next()) {
					lang = result.getString("lang");
				}
			}
			return lang;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean hasLang(Player p) {
		if (playerLangs.containsKey(p.getUniqueId())) {
			return true;
		}
		return hasLangSQL(p);
	}
	
	public boolean hasLangSQL(Player p) {
		return conn.getSqlTable().executeSelect("uuid = ?")
		        .dataSource(conn.getDatabase().getDataSource())
		        .statement(s -> s.setString(1, p.toString()))
		        .resultNext(r -> true)
		        .run(false, false);
	}
	
	public void setLang(Player p, boolean sql) {
		if(playerLangs.containsKey(p.getUniqueId())) {
			setLang(p, playerLangs.get(p.getUniqueId()), sql, false);
		} else {
			setLang(p, DEFAULT_LANGUAGE, sql, false);
		}
	}

	public void setLang(Player p, String string, boolean sql, boolean first) {
		ChangeLanguageEvent event = new ChangeLanguageEvent(p, string, first);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			return;
		}
		if(!event.getLanguage().equals(string)) {
			string = event.getLanguage();
		}
		playerLangs.put(p.getUniqueId(), string);
		if(!sql) {
			return;
		}
		if(hasLangSQL(p)) {
			conn.getSqlTable().executeUpdate("UPDATE `MitwLang` SET `lang` = ? WHERE `uuid` = ?;")
				.dataSource(conn.getDatabase().getDataSource()).statement(s -> {
					s.setString(1, playerLangs.get(p.getUniqueId()));
					s.setString(2, p.getUniqueId().toString());
				}).run();
		} else {
			conn.getSqlTable().executeInsert("?, ?")
				.dataSource(conn.getDatabase().getDataSource())
				.statement(s -> {
					s.setString(1, p.getUniqueId().toString());
					s.setString(2, playerLangs.get(p.getUniqueId()));
				}).run();
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if (hasLang(p)) {
			playerLangs.put(p.getUniqueId(), getLang(p));
		} else {
			setLang(p, DEFAULT_LANGUAGE, true, true);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		setLang(p, true);
	}
	
	public static class ChangeLanguageEvent extends Event{
		
		private static final HandlerList handlerlist = new HandlerList();

		private Player p;
		private String language;
		private boolean cancelled = false;
		private boolean first;
		
		public ChangeLanguageEvent(Player p, String language, boolean first) {
			this.p = p;
			this.language = language;
			this.first = first;
		}
		
		public Player getPlayer() {
			return p;
		}
		
		public String getLanguage() {
			return language;
		}
		
		public void setLanguage(String language) {
			this.language = language;
		}
		
		public boolean isFirstSet() {
			return first;
		}
		
		public boolean isCancelled() {
			return cancelled;
		}
		
		public void setCancelled(boolean cancel) {
			cancelled = cancel;
		}
		
		public HandlerList getHandlers() {
			return handlerlist;
		}
		
		public static HandlerList getHandlerList() {
			return handlerlist;
		}
	}

}
