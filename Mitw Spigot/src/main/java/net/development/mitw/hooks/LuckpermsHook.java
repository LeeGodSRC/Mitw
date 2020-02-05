package net.development.mitw.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckpermsHook {

	public static LuckPerms api;

	public static boolean hook() {
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			api = provider.getProvider();
		}
		return true;
	}

	public static String getPrefix(final Player p) {
		final User user = api.getUserManager().getUser(p.getName());
		if (user == null)
			return "";
		ContextManager contextManager = api.getContextManager();
		ImmutableContextSet contextSet = contextManager.getContext(user).orElseGet(contextManager::getStaticContext);
		CachedMetaData metaData = user.getCachedData().getMetaData(QueryOptions.contextual(contextSet));
		final String prefix = metaData.getPrefix();
		return prefix != null ? ChatColor.translateAlternateColorCodes('&', prefix) : "";
	}

	public static String getSuffix(final Player p) {
		final User user = api.getUserManager().getUser(p.getName());
		if (user == null)
			return "";
		ContextManager contextManager = api.getContextManager();
		ImmutableContextSet contextSet = contextManager.getContext(user).orElseGet(contextManager::getStaticContext);
		CachedMetaData metaData = user.getCachedData().getMetaData(QueryOptions.contextual(contextSet));
		final String suffix = metaData.getSuffix();
		return suffix != null ? ChatColor.translateAlternateColorCodes('&', suffix) : "";
	}
}
