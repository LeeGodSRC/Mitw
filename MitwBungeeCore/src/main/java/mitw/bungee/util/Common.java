package mitw.bungee.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class Common {
	public static void tell(CommandSender sender, String str) {
		sender.sendMessage(colored(str));
	}

	public static void tell(CommandSender sender, String... strs) {
		for (final String str : strs)
			sender.sendMessage(colored(str));
	}

	public static String colored(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	public static void tell(CommandSender sender, BaseComponent[] text) {
		sender.sendMessage(text);
	}

	public static void tell(CommandSender sender, TextComponent text) {
		sender.sendMessage(text);

	}
}
