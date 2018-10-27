package net.development.mitw.commands.param.defaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.development.mitw.commands.param.ParameterType;

public class FloatParameterType implements ParameterType<Float> {

	public Float transform(CommandSender sender, String source) {
		if (source.toLowerCase().contains("e")) {
			sender.sendMessage(ChatColor.RED + source + " is not a valid number.");
			return (null);
		}

		try {
			float parsed = Float.parseFloat(source);

			if (Float.isNaN(parsed) || !Float.isFinite(parsed)) {
				sender.sendMessage(ChatColor.RED + source + " is not a valid number.");
				return (null);
			}

			return (parsed);
		} catch (NumberFormatException exception) {
			sender.sendMessage(ChatColor.RED + source + " is not a valid number.");
			return (null);
		}
	}

	public List<String> tabComplete(Player sender, Set<String> flags, String source) {
		return (new ArrayList<>());
	}

}