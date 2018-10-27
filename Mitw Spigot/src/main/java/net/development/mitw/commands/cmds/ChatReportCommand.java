package net.development.mitw.commands.cmds;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.development.mitw.Mitw;
import net.development.mitw.commands.Command;
import net.development.mitw.commands.param.Parameter;
import net.development.mitw.language.LanguageAPI;
import net.development.mitw.utils.RV;

public class ChatReportCommand {

	private static final Cache<UUID, Long> longTime = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).maximumSize(10_000).build();
	private static final int COOLDOWN_SEC = 30;

	@Command(names = { "chatreport", "chreport", "creport" })
	public static void chatreport(final Player p, @Parameter(name = "word", wildcard = true) final String word) {
		final LanguageAPI api = Mitw.getInstance().getCoreLanguage();
		final long systemTime = System.currentTimeMillis();
		if (longTime.getIfPresent(p.getUniqueId()) != null) {
			api.send(p, "chatreport_incooldown", RV.o("<sec>", ((COOLDOWN_SEC - (systemTime - longTime.getIfPresent(p.getUniqueId())) / 1000)) + ""));
			return;
		}
		final String toReport = word.toLowerCase();
		Mitw.getInstance().getChatManager().getChatDB().putTempWord(toReport);
		longTime.put(p.getUniqueId(), systemTime);
		api.send(p, "chatreport_success", RV.o("<word>", toReport));
		return;
	}

}
