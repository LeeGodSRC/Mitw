package net.development.mitw.chat;

import org.bukkit.scheduler.BukkitRunnable;

import net.development.mitw.Mitw;
import net.development.mitw.chat.check.Check;
import net.development.mitw.chat.check.CheckType;

public class CopyDatatask extends BukkitRunnable {
	ChatDatabase db = Mitw.getInstance().getChatManager().getChatDB();

	@Override
	public void run() {
		Check.getCheck("high").setCheckExams(db.getAllWordsByType(CheckType.HIGH));
		Check.getCheck("low").setCheckExams(db.getAllWordsByType(CheckType.LOW));
		Check.getCheck("single").setCheckExams(db.getAllWordsByType(CheckType.SINGLE));
	}

}
