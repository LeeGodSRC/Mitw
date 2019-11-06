package net.development.mitw.commands.cmds;

import org.bukkit.entity.Player;

import net.development.mitw.Mitw;
import net.development.mitw.commands.Command;

public class HelpCommand {

	@Command(names = "help")
	public static void help(final Player player) {
		Mitw.getInstance().getHelpHandlers().forEach(helpHandler -> helpHandler.getHelp(player).forEach(player::sendMessage));
	}

}
