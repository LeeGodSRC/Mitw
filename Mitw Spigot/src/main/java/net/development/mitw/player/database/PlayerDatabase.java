package net.development.mitw.player.database;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.Setter;
import me.skymc.taboolib.mysql.builder.SQLColumn;
import me.skymc.taboolib.mysql.builder.SQLColumnType;
import me.skymc.taboolib.mysql.builder.SQLTable;
import net.development.mitw.Mitw;
import net.development.mitw.database.Database;

@Getter
@Setter
public class PlayerDatabase {

	private String ip;
	private int port;
	private String name;
	private String password;
	private String databaseName;

	private Database database;
	private SQLTable sqlTable;
	private SQLTable playerTable;

	public PlayerDatabase(String database) {
		this.databaseName = database;
	}

	public boolean connect() {
		try {
			database = new Database(Mitw.getInstance(), databaseName);
			createTable();
			Bukkit.getConsoleSender().sendMessage("§aMitw Language API SQL Connectted");
			return true;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void createTable() {
		sqlTable = new SQLTable("MitwLang")
				.addColumn(new SQLColumn(SQLColumnType.TEXT, "uuid"))
				.addColumn(new SQLColumn(SQLColumnType.TEXT, "lang"));

		sqlTable.executeUpdate(sqlTable.createQuery()).dataSource(database.getDataSource()).run();

		playerTable = new SQLTable("mitw_data")
				.addColumn(new SQLColumn(SQLColumnType.TEXT, "uuid"))
				.addColumn(new SQLColumn(SQLColumnType.INT, "coins"))
				.addColumn(new SQLColumn(SQLColumnType.INT, "mitwpass_level"))
				.addColumn(new SQLColumn(SQLColumnType.INT, "mitwpass_exp"));

		playerTable.executeUpdate(playerTable.createQuery()).dataSource(database.getDataSource()).run();
	}

	public boolean isConnect() {
		return !(database.getDataSource() != null);
	}

}
