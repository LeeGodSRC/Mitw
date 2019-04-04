package mitw.bungee.database;

import mitw.bungee.config.impl.MySQL;
import lombok.Getter;
import taboolib.mysql.builder.SQLHost;
import taboolib.mysql.builder.hikari.HikariHandler;

import javax.sql.DataSource;

public class Database {

	@Getter
	private final SQLHost sqlHost;
	@Getter
	private final DataSource dataSource;

	public Database(String database) {
		sqlHost = new SQLHost(MySQL.HOSTNAME, MySQL.USER, MySQL.PORT, MySQL.PASSWORD, database);
		dataSource = HikariHandler.createDataSource(sqlHost);

	}

}
