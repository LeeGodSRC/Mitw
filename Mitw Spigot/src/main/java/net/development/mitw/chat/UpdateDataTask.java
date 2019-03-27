package net.development.mitw.chat;

import net.development.mitw.uuid.UUIDCache;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.mitw.Mitw;
import net.development.mitw.chat.check.Check;
import net.development.mitw.chat.check.CheckType;

public class UpdateDataTask extends BukkitRunnable {

	@Override
	public void run() {
		UUIDCache.feteh();
		final ChatDatabase db = Mitw.getInstance().getChatManager().getChatDB();
		Check.getCheck("high").setCheckExams(db.getAllWordsByType(CheckType.HIGH));
		Check.getCheck("low").setCheckExams(db.getAllWordsByType(CheckType.LOW));
		Check.getCheck("single").setCheckExams(db.getAllWordsByType(CheckType.SINGLE));
	}

}
