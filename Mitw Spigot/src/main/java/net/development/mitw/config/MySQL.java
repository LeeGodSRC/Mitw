package net.development.mitw.config;

public class MySQL extends SimpleConfig {

	public MySQL(String fileName) {
		super(fileName);
	}

	public void loadVar() {
		HOSTNAME = getString("database.hostname");
		PORT = getString("database.port");
		USER = getString("database.user");
		PASSWORD = getString("database.password");
		DATABASE = getString("database.database");
		LANGUAGE_DATABASE = getString("database.languagedatabase");
	}


	public static void init() {
		final MySQL sql = new MySQL("MySQL.yml");
		sql.loadVar();
	}

	public static String HOSTNAME, PORT, USER, PASSWORD, DATABASE, LANGUAGE_DATABASE;

}