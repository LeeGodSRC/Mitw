package net.development.mitw.commands.param.defaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.development.mitw.Mitw;
import net.development.mitw.commands.param.ParameterType;

public class PlayerParameterType implements ParameterType<Player> {

	@Override
	public Player transform(final CommandSender sender, final String source) {
		if (sender instanceof Player && (source.equalsIgnoreCase("self") || source.equals("")))
			return ((Player) sender);

		final Player player = Mitw.getInstance().getServer().getPlayer(source);

		if (player == null) {
			sender.sendMessage(ChatColor.RED + "No player with the name " + source + " found.");
			return (null);
		}

		return (player);
	}

	@Override
	public List<String> tabComplete(final Player sender, final Set<String> flags, final String source) {
		final List<String> completions = new ArrayList<>();

		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (StringUtils.startsWithIgnoreCase(player.getName(), source)) {
				completions.add(player.getName());
			}
		}

		return completions;
	}

}