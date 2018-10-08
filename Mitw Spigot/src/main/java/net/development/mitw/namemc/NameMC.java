package net.development.mitw.namemc;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import net.development.mitw.Mitw;

public class NameMC implements PluginMessageListener {

	public NameMC(Mitw mitw) {
		mitw.getServer().getMessenger().registerIncomingPluginChannel(mitw, "NAMEMC | VOTE", this);
		mitw.getServer().getMessenger().registerOutgoingPluginChannel(mitw, "NAMEMC | VOTE");
	}

	@Override
	public void onPluginMessageReceived(String string, Player player, byte[] bytes) {

	}

}
