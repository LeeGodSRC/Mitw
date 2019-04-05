package mitw.bungee.config.impl;

public class MySQL extends Config {

	public MySQL(final String fileName) {
		super(fileName);
	}

	public void loadVar() {
		HOSTNAME = getString("database.hostname");
		PORT = getString("database.port");
		USER = getString("database.user");
		PASSWORD = getString("database.password");
		DATABASE = getString("database.database");
		LANGUAGE_DATABASE = getString("database.languagedatabase");
		CHAT_DATABASE = getString("database.chatdatabase");
		save();
	}


	public static void init() {
		final MySQL sql = new MySQL("MySQL");
		sql.loadVar();
	}

	public static String HOSTNAME, PORT, USER, PASSWORD, DATABASE, LANGUAGE_DATABASE, CHAT_DATABASE;

}
