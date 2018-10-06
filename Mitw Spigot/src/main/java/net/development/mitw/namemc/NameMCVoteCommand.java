package net.development.mitw.namemc;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import net.development.mitw.Mitw;

public class NameMCVoteCommand extends BukkitCommand {

	public NameMCVoteCommand() {
		super("vote");
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) {
		final Player player = (Player)sender;
		if (args.length > 0) {
			return true;
		}
		Mitw.getInstance().getCoreLanguage().translateArrays(player, "nameMCVoteInfo")
		.forEach(sender::sendMessage);
		return false;
	}

}
