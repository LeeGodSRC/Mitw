package net.development.mitw.packetlistener;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;

import lombok.Getter;
import net.development.mitw.packetlistener.newer.PacketIniter_Newer;

public class PacketHandler {

	@Getter
	private final Set<PacketListener> packetListeners = new HashSet<>();

	@Getter
	private static PacketHandler instance;

	@Getter
	private PacketListenerInit packetListenerInit;

	public PacketHandler() {
		instance = this;

		try {
			packetListenerInit = new PacketIniter_Newer();
			Bukkit.broadcastMessage("newer channel");

			packetListenerInit.addServerChannel();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public boolean register(PacketListener packetListener) {
		if(packetListeners.contains(packetListener)) {
			return false;
		}
		packetListeners.add(packetListener);
		return true;
	}

	public boolean unregister(PacketListener packetListener) {
		return packetListeners.remove(packetListener);
	}

}
