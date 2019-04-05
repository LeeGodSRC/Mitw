package mitw.bungee.language.types;

import mitw.bungee.language.ILanguageData;
import mitw.bungee.language.LanguageSQLConnection;
import mitw.bungee.util.FastUUID;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SQLLanguageData implements Listener, ILanguageData {

	private static Map<UUID, String> playerLangs = new HashMap<>();

	@Getter @Setter private Plugin plugin;
	@Getter @Setter private LanguageSQLConnection conn;

	private final ExecutorService langExecutor = Executors.newSingleThreadExecutor();

	public SQLLanguageData(final Plugin plugin, final LanguageSQLConnection conn) {
		this.plugin = plugin;
		this.conn = conn;
		ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
		conn.connect();
	}

	@Override
	public String getLang(final UUID uuid) {
		if (playerLangs.containsKey(uuid))
			return playerLangs.get(uuid);
		return conn.getSqlTable().executeSelect("uuid = ?")
				.dataSource(conn.getDatabase().getDataSource())
				.statement(s -> s.setString(1, FastUUID.toString(uuid)))
				.resultNext(r -> {
					return r.getString("lang");
				}).run("", "");
	}

	@Override
	public boolean hasLang(final ProxiedPlayer p) {
		if (playerLangs.containsKey(p.getUniqueId()))
			return true;
		return hasLangSQL(p);
	}

	public boolean hasLangSQL(final ProxiedPlayer p) {
		return conn.getSqlTable().executeSelect("uuid = ?")
				.dataSource(conn.getDatabase().getDataSource())
				.statement(s -> s.setString(1, p.getUniqueId().toString()))
				.resultNext(r -> true)
				.run(false, false);
	}

	@Override
	public void setLang(ProxiedPlayer p, String lang, boolean first) {
		this.setLang(p, lang, true, first);
	}

    @Override
    public void setLangWithoutSave(ProxiedPlayer proxiedPlayer, String lang, boolean first) {
        this.setLang(proxiedPlayer, lang, false, true);
    }

    public void setLang(final ProxiedPlayer p, String string, final boolean sql, final boolean first) {
		final ChangeLanguageEvent event = new ChangeLanguageEvent(p, string, first);
	    ProxyServer.getInstance().getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		if(!event.getLanguage().equals(string)) {
			string = event.getLanguage();
		}
		playerLangs.put(p.getUniqueId(), string);
		if(!sql)
			return;
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
	public void onLogin(final PostLoginEvent e) {
		final ProxiedPlayer p = e.getPlayer();
		langExecutor.execute(() -> {
			if (hasLang(p)) {
				playerLangs.put(p.getUniqueId(), getLang(p));
			} else {
				setLang(p, DEFAULT_LANGUAGE, true, true);
			}
		});
	}

	@EventHandler
	public void onQuit(final ServerDisconnectEvent e) {
		final ProxiedPlayer p = e.getPlayer();
		playerLangs.remove(p.getUniqueId());
	}

}
