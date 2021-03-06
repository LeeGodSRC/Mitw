package net.development.mitw.chat;

import java.util.ArrayList;
import java.util.List;

import me.skymc.taboolib.mysql.builder.SQLColumn;
import me.skymc.taboolib.mysql.builder.SQLColumnType;
import me.skymc.taboolib.mysql.builder.SQLTable;
import net.development.mitw.Mitw;
import net.development.mitw.chat.check.CheckType;
import net.development.mitw.config.MySQL;
import net.development.mitw.database.Database;

public class ChatDatabase {

	private final Database database;
	private final SQLTable table;

	private final SQLTable tempWordTable;

	public ChatDatabase(final Mitw plugin) {
		database = new Database(plugin, MySQL.CHAT_DATABASE);
		table = new SQLTable("chat"
				, new SQLColumn(SQLColumnType.TEXT, "words")
				, new SQLColumn(SQLColumnType.TEXT, "level"));
		table.executeUpdate(table.createQuery()).dataSource(database.getDataSource()).run();
		// --------
		tempWordTable = new SQLTable("tempWord"
				, new SQLColumn(SQLColumnType.TEXT, "words"));
		tempWordTable.executeUpdate(tempWordTable.createQuery()).dataSource(database.getDataSource()).run();
	}

	public List<String> getAllWordsByType(final CheckType type){
		final List<String> words = new ArrayList<>();
		try {
			table.executeQuery("SELECT words from chat WHERE level = ?")
			.dataSource(database.getDataSource())
			.statement(s -> s.setString(1, type.name().toLowerCase()))
			.result(r -> {
				if (r.isBeforeFirst()) {
					while (r.next()) {
						words.add(r.getString("words"));
					}
				}
				return r;
			}).run();
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
		return words;
	}

	public List<String> getTop54TempWords() {
		final List<String> words = new ArrayList<>();
		try {
			tempWordTable.executeQuery("SELECT words from tempWord")
			.dataSource(database.getDataSource())
			.result(r -> {
				if(r.isBeforeFirst()) {
					int count = 0;
					while (r.next() && count < 54) {
						words.add(r.getString("words"));
						count++;
					}
				}
				return r;
			}).run();
		}catch (final Exception e) {
			e.printStackTrace();
		}
		return words;
	}

	public boolean putTempWord(String word){
		if(containsTempWord(word))
			return false;
		try {
			tempWordTable.executeInsert("?")
			.dataSource(database.getDataSource())
			.statement(s -> {
				s.setString(1, word);
			}).run();
			return true;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean putWords(final String word, final CheckType type) {
		if (containsWord(word)) {
			try {
				table.executeUpdate("UPDATE chat SET level = ? WHERE words = ?;")
				.dataSource(database.getDataSource())
				.statement(s -> {
					s.setString(1, type.name().toLowerCase());
					s.setString(2, word);
				}).run();
				return true;
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		try {
			table.executeInsert("?, ?")
			.dataSource(database.getDataSource())
			.statement(s -> {
				s.setString(1, word);
				s.setString(2, type.name().toLowerCase());
			}).run();
			return true;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeWords(final String word) {
		if (containsWord(word)) {
			try {
				table.executeUpdate("DELETE FROM chat WHERE words = ?")
				.dataSource(database.getDataSource())
				.statement(s -> s.setString(1, word))
				.run();
				return true;
			} catch (final Exception e) {
				e.printStackTrace();
				return false;
			}
		} else
			return false;
	}

	public boolean removeTempWord(final String word) {
		if (containsTempWord(word)) {
			try {
				tempWordTable.executeUpdate("DELETE FROM tempWord WHERE words = ?;")
				.dataSource(database.getDataSource())
				.statement(s -> s.setString(1, word))
				.run();
				return true;
			} catch (final Exception e) {
				e.printStackTrace();
				return false;
			}
		} else
			return false;
	}

	private boolean containsWord(final String word) {
		return table.executeSelect("words = ?").dataSource(database.getDataSource()).statement(s -> {
			s.setString(1, word);
		}).resultNext(r -> true).run(false, false);
	}

	private boolean containsTempWord(final String word) {
		return tempWordTable.executeSelect("words = ?").dataSource(database.getDataSource()).statement(s -> {
			s.setString(1, word);
		}).resultNext(r -> true).run(false, false);
	}

}
