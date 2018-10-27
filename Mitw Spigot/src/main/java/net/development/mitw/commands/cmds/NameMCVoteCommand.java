package net.development.mitw.commands.cmds;

import org.bukkit.entity.Player;

import net.development.mitw.Mitw;
import net.development.mitw.commands.Command;

public class NameMCVoteCommand {

	@Command(names = "votes")
	public static void execute(final Player player) {
		Mitw.getInstance().getCoreLanguage().translateArrays(player, "nameMCVoteInfo")
		.forEach(player::sendMessage);
		return;
	}

}
