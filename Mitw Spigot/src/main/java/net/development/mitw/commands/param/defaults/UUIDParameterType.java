package net.development.mitw.commands.param.defaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.development.mitw.Mitw;
import net.development.mitw.commands.param.ParameterType;

public class UUIDParameterType implements ParameterType<UUID> {

	@Override
	public UUID transform(final CommandSender sender, final String source) {
		if (sender instanceof Player && (source.equalsIgnoreCase("self") || source.equals("")))
			return (((Player) sender).getUniqueId());

		try {
			return UUID.fromString(source);
		} catch (final Exception e) {
			sender.sendMessage(ChatColor.RED + "That UUID could not be parsed.");
		}

		return null;
	}

	@Override
	public List<String> tabComplete(final Player sender, final Set<String> flags, final String source) {
		final List<String> completions = new ArrayList<>();

		for (final Player player : Mitw.getInstance().getServer().getOnlinePlayers()) {
			if (StringUtils.startsWithIgnoreCase(player.getName(), source)) {
				completions.add(player.getName());
			}
		}

		return (completions);
	}

}