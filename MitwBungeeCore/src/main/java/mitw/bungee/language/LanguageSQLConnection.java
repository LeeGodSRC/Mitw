package mitw.bungee.language;

import mitw.bungee.database.Database;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import taboolib.mysql.builder.SQLColumn;
import taboolib.mysql.builder.SQLColumnType;
import taboolib.mysql.builder.SQLTable;

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
			database = new Database(databaseName);
			createTable();
			ProxyServer.getInstance().getConsole().sendMessage("§aMitw Language API SQL Connectted");
			return true;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void createTable() {
		sqlTable = new SQLTable("MitwLang")
				.addColumn(new SQLColumn(SQLColumnType.TEXT, "uuid"))
				.addColumn(new SQLColumn(SQLColumnType.TEXT, "lang")); //might be problem

		sqlTable.executeUpdate(sqlTable.createQuery()).dataSource(database.getDataSource()).run();
	}

	public boolean isConnect() {
		return !(database.getDataSource() != null);
	}

}
