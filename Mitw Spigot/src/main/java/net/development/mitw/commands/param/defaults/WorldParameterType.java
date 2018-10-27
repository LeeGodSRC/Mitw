package net.development.mitw.commands.param.defaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.development.mitw.Mitw;
import net.development.mitw.commands.param.ParameterType;

public class WorldParameterType implements ParameterType<World> {

	@Override
	public World transform(final CommandSender sender, final String source) {
		final World world = Mitw.getInstance().getServer().getWorld(source);

		if (world == null) {
			sender.sendMessage(ChatColor.RED + "No world with the name " + source + " found.");
			return (null);
		}

		return (world);
	}

	@Override
	public List<String> tabComplete(final Player sender, final Set<String> flags, final String source) {
		final List<String> completions = new ArrayList<>();

		for (final World world : Mitw.getInstance().getServer().getWorlds()) {
			if (StringUtils.startsWithIgnoreCase(world.getName(), source)) {
				completions.add(world.getName());
			}
		}

		return (completions);
	}

}