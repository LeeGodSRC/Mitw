package mitw.bungee.listener;

import mitw.bungee.Mitw;
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
	public void onQuit(final ServerDisconnectEvent e) {
		Mitw.replys.remove(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onKick(final ServerKickEvent ev) {
		ServerInfo kickedFrom;
		if (ev.getPlayer().getServer() != null) {
			kickedFrom = ev.getPlayer().getServer().getInfo();
		} else if (Mitw.INSTANCE.getProxy().getReconnectHandler() != null) {
			kickedFrom = Mitw.INSTANCE.getProxy().getReconnectHandler().getServer(ev.getPlayer());
		} else {
			kickedFrom = AbstractReconnectHandler.getForcedHost(ev.getPlayer().getPendingConnection());
			if (kickedFrom == null) {
				ev.getPlayer().disconnect("找不到預設伺服器 請稍後重登看看");
			}
		}

		if (!ev.isCancelled() && ev.getState() != ServerKickEvent.State.CONNECTING) {
			final ServerInfo kickTo = Mitw.INSTANCE.getProxy().getServerInfo("waiting");
			if (kickedFrom == null || !kickedFrom.equals(kickTo)) {
				ev.getPlayer().sendMessage(ev.getKickReasonComponent());
				ev.setCancelled(true);
				ev.setCancelServer(kickTo);
			}
		}
	}

	@EventHandler
	public void onConnect(final ServerConnectedEvent e) {
		final ProxiedPlayer p = e.getPlayer();
		if (!p.hasPermission("mitw.admin"))
			return;
		for (final ProxiedPlayer to : ProxyServer.getInstance().getPlayers())
			if (to.hasPermission("mitw.admin") && p.getServer() != null) {
				to.sendMessage("§7[§6§lStaff§7]§7 Staff §e" + p.getName() + " §7連結到 §b" + e.getServer().getInfo().getName() + " §7從 §c"
						+ p.getServer().getInfo().getName() + "§7 !");
			}
	}
}