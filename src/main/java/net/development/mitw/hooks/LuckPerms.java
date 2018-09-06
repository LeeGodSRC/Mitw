package net.development.mitw.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import net.md_5.bungee.api.ChatColor;

public class LuckPerms {
	
	public static LuckPermsApi api;
	
	public static boolean hook() {
		RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
		if (provider != null) {
		    api = provider.getProvider();
		}
		return true;
	}
	
	public static String getPrefix(Player p) {
		User user = api.getUser(p.getName());
		MetaData metaData = user.getCachedData().getMetaData(Contexts.global());
		String prefix = metaData.getPrefix();
		return ChatColor.translateAlternateColorCodes('&', prefix);
	}

	public static String getSuffix(Player p) {
		User user = api.getUser(p.getName());
		MetaData metaData = user.getCachedData().getMetaData(Contexts.global());
		String suffix = metaData.getSuffix();
		return ChatColor.translateAlternateColorCodes('&', suffix);
	}
}
