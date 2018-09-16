package net.development.mitw.language;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.Setter;
import me.skymc.taboolib.mysql.builder.SQLColumn;
import me.skymc.taboolib.mysql.builder.SQLColumnOption;
import me.skymc.taboolib.mysql.builder.SQLColumnType;
import me.skymc.taboolib.mysql.builder.SQLTable;
import net.development.mitw.Mitw;
import net.development.mitw.database.Database;

@Getter
@Setter
public class LanguageSQLConnection {

	private String ip;
	private int port;
	private String name;
	private String password;
	private String databaseName;
	
	private Database database;
	private SQLTable sqlTable;

	public LanguageSQLConnection(String database) {
		this.databaseName = database;
	}

	public boolean connect() {
		try {
			database = new Database(Mitw.getInstance(), databaseName);
			createTable();
			Bukkit.getConsoleSender().sendMessage("§aMitw Language API SQL Connectted");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void createTable() {
		sqlTable = new SQLTable("MitwLang")
				.addColumn(new SQLColumn(SQLColumnType.VARCHAR, "uuid", SQLColumnOption.UNIQUE_KEY, SQLColumnOption.PRIMARY_KEY))
				.addColumn(new SQLColumn(SQLColumnType.VARCHAR, "lang", SQLColumnOption.UNIQUE_KEY)); //might be problem
		
		sqlTable.executeQuery(sqlTable.createQuery()).run();
	}

	public boolean isConnect() {
		return !(database.getDataSource() != null);
	}

}
