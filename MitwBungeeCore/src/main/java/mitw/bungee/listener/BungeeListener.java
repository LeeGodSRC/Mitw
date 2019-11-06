package mitw.bungee.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import mitw.bungee.Mitw;
import mitw.bungee.util.RV;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BungeeListener implements Listener {

	private static String[] LOBBIES = {"waiting", "lobby-2", "lobby-3"};
	private Random random = new Random();

	public BungeeListener() {
	}

	private List<UUID> locked = new ArrayList<>();

	@EventHandler
	public void onQuit(final PlayerDisconnectEvent e) {
		locked.remove(e.getPlayer().getUniqueId());
		Mitw.replys.remove(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onConnecting(final ServerConnectEvent event) {
		if (!locked.contains(event.getPlayer().getUniqueId()) && event.getTarget().getName().equals("waiting")) {
			locked.add(event.getPlayer().getUniqueId());
			List<String> aliveLobbies = Stream.of(LOBBIES).filter(Mitw.INSTANCE.getKeepAliveHandler()::isServerAlive).collect(Collectors.toList());
			if (aliveLobbies.size() == 0) {
				return;
			}
			String result = aliveLobbies.get(random.nextInt(aliveLobbies.size()));
			event.setTarget(ProxyServer.getInstance().getServerInfo(result));
		}
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