package net.development.mitw.config;

public class MySQL extends SimpleConfig {

	public MySQL(String fileName) {
		super(fileName);
		setPathPrefix("database");
	}

	public void loadVar() {
		HOSTNAME = getString("hostname");
		PORT = getString("port");
		USER = getString("user");
		PASSWORD = getString("password");
		DATABASE = getString("database");
		LANGUAGE_DATABASE = getString("languagedatabase");
	}


	public static void init() {
		final MySQL sql = new MySQL("MySQL.yml");
		sql.loadVar();
	}

	public static String HOSTNAME, PORT, USER, PASSWORD, DATABASE, LANGUAGE_DATABASE;

}
