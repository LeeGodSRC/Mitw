package net.development.mitw.command;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.development.mitw.Mitw;
import net.development.mitw.language.LanguageAPI;
import net.development.mitw.utils.RV;

public class ChatReportCommand extends Command {
	private final Cache<UUID, Long> longTime = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).maximumSize(10_000).build();
	private final int COOLDOWN_SEC = 30;

	public ChatReportCommand() {
		super("chatreport");
		setAliases(Arrays.asList("chreport", "creport"));
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		final Player p = (Player) sender;
		final LanguageAPI api = Mitw.getInstance().getCoreLanguage();
		final long systemTime = System.currentTimeMillis();
		if (args.length < 1) {
			api.send(p, "chatreport_usage");
			return false;
		}
		if (longTime.getIfPresent(p.getUniqueId()) != null) {
			api.send(p, "chatreport_incooldown", RV.o("<sec>", ((COOLDOWN_SEC - (systemTime - longTime.getIfPresent(p.getUniqueId())) / 1000)) + ""));
			return false;
		}
		final StringBuilder builder = new StringBuilder(args[0]);
		for (int a = 1; a < args.length; a++)
			builder.append(" " + args[a]);
		final String toReport = builder.toString().toLowerCase();
		Mitw.getInstance().getChatManager().getChatDB().putTempWord(toReport);
		longTime.put(p.getUniqueId(), systemTime);
		api.send(p, "chatreport_success", RV.o("<word>", toReport));
		return true;
	}

}
