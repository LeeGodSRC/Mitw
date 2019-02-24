package mitw.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MitwLanguage implements Listener{

	public static enum LangType {
		CONFIG, CLASS
	};

	private static final String DEFAULT_LANGUAGE = "zh_tw";
	private static Map<UUID, String> playerLangs = new HashMap<>();

	private final Map<String, List<String>> savedMessages = new HashMap<>();
	private Object clazz;
	private final SQLConnection conn;

	public MitwLanguage(final SQLConnection conn, final Object clazz) {
		this.clazz = clazz;
		this.conn = conn;
	}

	public Object getObject() {
		return clazz;
	}

	public void setObject(final Object clazz) {
		this.clazz = clazz;
	}

	public void setClass(final Class<?> clazz, final Class<?>... methods) {
		try {
			this.clazz = clazz.getConstructor(methods).newInstance();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, List<String>> getSavedMessages() {
		return savedMessages;
	}

	public String getLang(final ProxiedPlayer p) {
		if (playerLangs.containsKey(p.getUniqueId()))
			return playerLangs.get(p.getUniqueId());
		try {
			final PreparedStatement ps = conn.getConnection().prepareStatement("SELECT * FROM `MitwLang` WHERE `uuid` = ?;");
			ps.setString(1, p.getUniqueId().toString());
			ps.executeQuery();
			final ResultSet result = ps.getResultSet();
			String lang = null;
			if(result.isBeforeFirst()) {
				while(result.next()) {
					lang = result.getString("lang");
				}
			}
			return lang;
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasLang(final ProxiedPlayer p) {
		if (playerLangs.containsKey(p.getUniqueId()))
			return true;
		return hasLangSQL(p);
	}

	public boolean hasLangSQL(final ProxiedPlayer p) {
		try {
			final PreparedStatement ps = conn.getConnection().prepareStatement("SELECT * FROM `MitwLang` WHERE `uuid` = ?;");
			ps.setString(1, p.getUniqueId().toString());
			ps.executeQuery();
			final ResultSet result = ps.getResultSet();
			return result.next();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setLang(final ProxiedPlayer p, final boolean sql) {
		if(playerLangs.containsKey(p.getUniqueId())) {
			setLang(p, playerLangs.get(p.getUniqueId()), sql, false, false);
		} else {
			setLang(p, DEFAULT_LANGUAGE, sql, false, false);
		}
	}

	public void setLang(final ProxiedPlayer p, final String string, final boolean sql, final boolean first, final boolean sync) {
		playerLangs.put(p.getUniqueId(), string);
		if(!sql)
			return;if(sync) {
				if(hasLangSQL(p)) {
					try {
						final PreparedStatement ps = conn.getConnection().prepareStatement("UPDATE `MitwLang` SET `lang` = ? WHERE `uuid` = ?;");
						ps.setString(1, playerLangs.get(p.getUniqueId()));
						ps.setString(2, p.getUniqueId().toString());
						ps.executeUpdate();
					} catch (final SQLException e) {
						e.printStackTrace();
					}
				} else {
					try {
						final PreparedStatement ps = conn.getConnection().prepareStatement("INSERT INTO `MitwLang` (uuid, lang) VALUES (?, ?)");
						ps.setString(1, p.getUniqueId().toString());
						ps.setString(2, playerLangs.get(p.getUniqueId()));
						ps.executeUpdate();
					} catch (final SQLException e) {
						e.printStackTrace();
					}
				}
				return;
			}
			if(hasLangSQL(p)) {
				try {
					final PreparedStatement ps = conn.getConnection().prepareStatement("UPDATE `MitwLang` SET `lang` = ? WHERE `uuid` = ?;");
					ps.setString(1, playerLangs.get(p.getUniqueId()));
					ps.setString(2, p.getUniqueId().toString());
					ps.executeUpdate();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			} else {
				try {
					final PreparedStatement ps = conn.getConnection().prepareStatement("INSERT INTO `MitwLang` (uuid, lang) VALUES (?, ?)");
					ps.setString(1, p.getUniqueId().toString());
					ps.setString(2, playerLangs.get(p.getUniqueId()));
					ps.executeUpdate();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
	}

	public String translate(final ProxiedPlayer p, final String ofrom) {
		final String lang = getLang(p);
		final String from = lang+"."+ofrom;
		if(savedMessages.containsKey(from))
			return savedMessages.get(from).get(0);
		else {
			String to = null;
			boolean found = false;
			try {
				Field field = clazz.getClass().getDeclaredField(from.replace(".", "_"));
				Object object = field.get(clazz);
				if(object == null) {
					field = clazz.getClass().getDeclaredField((DEFAULT_LANGUAGE+"_"+ofrom).replace(".", "_"));
					field.setAccessible(true);
					object = field.get(clazz);
				} else {
					found = true;
				}
				to = (String)object;
			} catch (final Exception e) {
				ProxyServer.getInstance().getConsole().sendMessage("Cant get string field "+from.replace(".", "_")+" from "+clazz.getClass().getName()+" from player "+p.getName()+" !");
				return "null";
			}
			to = ChatColor.translateAlternateColorCodes('&', to);
			if(found) {
				savedMessages.put(from, Arrays.asList(to));
			}
			return to;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> translateArrays(final ProxiedPlayer p, final String ofrom) {
		final String lang = getLang(p);
		final String from = lang+"."+ofrom;
		if(savedMessages.containsKey(from))
			return savedMessages.get(from);
		else {
			List<String> to = null;
			boolean found = false;
			try {
				Field field = clazz.getClass().getDeclaredField(from.replace(".", "_"));
				Object object = field.get(clazz);
				if(object == null) {
					field = clazz.getClass().getDeclaredField((DEFAULT_LANGUAGE+"_"+ofrom).replace(".", "_"));
					field.setAccessible(true);
					object = field.get(clazz);
				} else {
					found = true;
				}
				to = (List<String>)object;
			} catch (final Exception e) {
				ProxyServer.getInstance().getConsole().sendMessage("Cant get string field "+from.replace(".", "_")+" from "+clazz.getClass().getName()+" from player "+p.getName()+" !");
				return Arrays.asList("null");
			}
			for(int i = 0; i < to.size() ; i++) {
				to.set(i, ChatColor.translateAlternateColorCodes('&', to.get(i)));
			}
			if(found) {
				savedMessages.put(from, to);
			}
			return to;
		}
	}

	@EventHandler
	public void onLogin(final PostLoginEvent e) {
		final ProxiedPlayer p = e.getPlayer();
		if(hasLang(p)) {
			playerLangs.put(p.getUniqueId(), getLang(p));
		} else {
			setLang(p, DEFAULT_LANGUAGE, true, true, true);
		}
	}

	@EventHandler
	public void onQuit(final PlayerDisconnectEvent e) {
		final ProxiedPlayer p = e.getPlayer();
		playerLangs.remove(p.getUniqueId());
	}

	public void enable() {
		for (final ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			if(hasLang(p)) {
				playerLangs.put(p.getUniqueId(), getLang(p));
			} else {
				setLang(p, DEFAULT_LANGUAGE, true, true, true);
			}
		}
	}

	public static class SQLConnection {

		private final String ip;
		private final int port;
		private final String name;
		private final String password;
		private final String database;
		private Connection connection;

		public SQLConnection(final String ip, final int port, final String name, final String password, final String database) {
			this.ip = ip;
			this.port = port;
			this.name = name;
			this.password = password;
			this.database = database;
		}

		public boolean connect() {
			try {
				connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database, this.name,
						this.password);

				ProxyServer.getInstance().getConsole().sendMessage("§aMitw Language API SQL Connectted");
				createTable();
				return true;
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		private void createTable() {
			query("CREATE TABLE IF NOT EXISTS `MitwLang` (`uuid` VARCHAR(60), `lang` VARCHAR(60))");
		}

		public boolean isConnect() {
			return !(connection == null);
		}

		public Connection getConnection() {
			return connection;
		}

		public void query(final String q) {
			query(q, true);
		}

		private void query(final String q, final boolean first) {
			if (isConnect()) {
				PreparedStatement ps = null;
				try {
					ps = connection.prepareStatement(q);
					ps.execute();
				} catch (final SQLException e) {
					if (first) {
						query(q, false);
					} else {
						e.printStackTrace();
					}
				} finally {
					if (ps != null) {
						try {
							ps.close();
						} catch (final SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		public ResultSet getResult(final String q) {
			return getResult(q, true);
		}

		private ResultSet getResult(final String q, final boolean info) {
			if (isConnect()) {
				Statement ps = null;
				try {
					ps = connection.createStatement();
					final ResultSet rs = ps.executeQuery(q);
					return rs;
				} catch (final SQLException e) {
					if(info) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}
	}

}
