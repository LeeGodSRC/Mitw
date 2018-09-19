package net.development.mitw.database;

import javax.sql.DataSource;

import lombok.Getter;
import me.skymc.taboolib.mysql.builder.SQLHost;
import me.skymc.taboolib.mysql.builder.hikari.HikariHandler;
import net.development.mitw.Mitw;
import net.development.mitw.config.MySQL;

public class Database {

	@Getter
	private final SQLHost sqlHost;
	@Getter
	private final DataSource dataSource;

	public Database(Mitw plugin, String database) {
		sqlHost = new SQLHost(MySQL.HOSTNAME, MySQL.USER, MySQL.PORT, MySQL.PASSWORD, database);
		dataSource = HikariHandler.createDataSource(sqlHost);

	}

}
