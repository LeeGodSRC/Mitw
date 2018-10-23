package net.development.mitw.command;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.development.mitw.Mitw;
import net.development.mitw.language.LanguageAPI;

public class ChatReportCommand extends Command {

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
		if (args.length < 1) {
			api.send(p, "chatreport_usage");
			return false;
		}
		return true;
	}

}

