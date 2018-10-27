package net.development.mitw.commands.param;

import java.util.List;
import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ParameterType<T> {

	T transform(CommandSender sender, String source);

	List<String> tabComplete(Player sender, Set<String> flags, String source);

}