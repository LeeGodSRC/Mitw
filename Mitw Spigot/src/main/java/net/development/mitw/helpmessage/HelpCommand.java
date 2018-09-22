package net.development.mitw.helpmessage;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import net.development.mitw.Mitw;

public class HelpCommand extends BukkitCommand {

	public HelpCommand() {
		super("help");

		setAliases(Arrays.asList("gamehelp"));
	}

	@Override
	public boolean execute(CommandSender sender, String cmd, String[] args) {

		Mitw.getInstance().getHelpHandlers().forEach(helpHandler -> helpHandler.getHelp().forEach(sender::sendMessage));

		return false;
	}

}