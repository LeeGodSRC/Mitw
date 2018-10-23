package net.development.mitw.utils;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class Common {
	public static void tell(CommandSender sender, String str) {
		sender.sendMessage(colored(str));
	}

	public static String colored(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	public static void tell(CommandSender sender, String... msgs) {
		for (final String s : msgs)
			sender.sendMessage(colored(s));
	}

	public static void registerCommand(Command command) {
		try {
			final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
			commandMap.register(command.getLabel(), command);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
