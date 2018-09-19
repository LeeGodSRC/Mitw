package net.development.mitw.utils;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class Common {
	public static void tell(CommandSender sender, String str) {
		sender.sendMessage(colored(str));
	}

	public static String colored(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
}
