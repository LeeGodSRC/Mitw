package net.development.mitw.database;

import javax.sql.DataSource;

import lombok.Getter;
import me.skymc.taboolib.mysql.builder.SQLHost;
import me.skymc.taboolib.mysql.builder.hikari.HikariHandler;
import net.development.mitw.Mitw;

public class Database {
	
	@Getter
	private SQLHost sqlHost;
	@Getter
	private DataSource dataSource;
	
	private String HostName;
	private String Port;
	private String User;
	private String Password;
	private String Database;

	public Database(Mitw plugin, String database) {
		
		HostName = plugin.getConfigManager().getMainConfig().getString("database.hostname");
		Port = plugin.getConfigManager().getMainConfig().getString("database.port");
		User = plugin.getConfigManager().getMainConfig().getString("database.user");
		Password = plugin.getConfigManager().getMainConfig().getString("database.password");
		Database = database;
		
		sqlHost = new SQLHost(HostName, User, Port, Password, Database);
		dataSource = HikariHandler.createDataSource(sqlHost);
		
	}

}
