package net.development.mitw.commands.param.defaults;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableList;

import net.development.mitw.commands.param.ParameterType;
import net.development.mitw.utils.ItemUtil;

public class ItemStackParameterType implements ParameterType<ItemStack> {

	@Override
	public ItemStack transform(final CommandSender sender, final String source) {
		final ItemStack item = ItemUtil.get(source);

		if (item == null) {
			sender.sendMessage(ChatColor.RED + "No item with the name " + source + " found.");
			return null;
		}

		return item;
	}

	@Override
	public List<String> tabComplete(final Player sender, final Set<String> flags, final String source) {
		return ImmutableList.of(); // it would probably be too intensive to go through all the aliases
	}

}
