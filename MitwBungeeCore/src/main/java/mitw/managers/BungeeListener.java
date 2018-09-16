package mitw.managers;

import mitw.Bungee;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListener implements Listener {
	public BungeeListener() {
	}

	@EventHandler
	public void onQuit(ServerDisconnectEvent e) {
		Bungee.replys.remove(e.getPlayer().getUniqueId());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onKick(ServerKickEvent ev) {
		ServerInfo kickedFrom = null;
		if (ev.getPlayer().getServer() != null)
			kickedFrom = ev.getPlayer().getServer().getInfo();
		else if (Bungee.ins.getProxy().getReconnectHandler() != null)
			kickedFrom = Bungee.ins.getProxy().getReconnectHandler().getServer(ev.getPlayer());
		else {
			kickedFrom = AbstractReconnectHandler.getForcedHost(ev.getPlayer().getPendingConnection());
			if (kickedFrom == null)
				ev.getPlayer().disconnect("找不到預設伺服器 請稍後重登看看");
		}

		if (!ev.isCancelled()) {
			ServerInfo kickTo = Bungee.ins.getProxy().getServerInfo("waiting");
			if (kickedFrom == null || !kickedFrom.equals(kickTo)) {
				ev.setCancelled(true);
				ev.setCancelServer(kickTo);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onConnect(ServerConnectedEvent e) {
		ProxiedPlayer p = e.getPlayer();
		if (!p.hasPermission("mitw.admin")) {
			return;
		}
		for (ProxiedPlayer to : ProxyServer.getInstance().getPlayers())
			if (to.hasPermission("mitw.admin") && p.getServer() != null)
				to.sendMessage("§7[§6§lStaff§7]§7 Staff §e" + p.getName() + " §7Connected to §b" + e.getServer().getInfo().getName() + " §7from §c"
						+ p.getServer().getInfo().getName() + "§7 !");
	}
}