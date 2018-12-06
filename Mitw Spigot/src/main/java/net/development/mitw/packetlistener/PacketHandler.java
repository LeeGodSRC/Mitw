package net.development.mitw.packetlistener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.entity.Player;

import lombok.Getter;
import net.development.mitw.Mitw;
import net.development.mitw.packetlistener.channel.ChannelWrapper;
import net.development.mitw.packetlistener.newer.Protocol_Newer;

public class PacketHandler {

	@Getter
	private final Set<PacketListener> packetListeners = new HashSet<>();

	@Getter
	private static PacketHandler instance;

	@Getter
	private Protocol_Newer packetListenerInit;

	public PacketHandler() {
		instance = this;

		try {
			packetListenerInit = new Protocol_Newer(Mitw.getInstance()) {
				@Override
				public Object onPacketInAsync(final Player sender, final ChannelWrapper channel, final Object packet) {

					Object owner = null;

					if (sender != null) {
						owner = sender;
					} else {
						owner = channel;
					}

					final PacketEvent packetEvent = new PacketEvent(owner, packet);
					for (final PacketListener packetListener : packetListeners) {
						packetListener.in(packetEvent);
					}

					if (packetEvent.isCancelled())
						return null;

					return super.onPacketInAsync(sender, channel, packet);
				}
				@Override
				public Object onPacketOutAsync(final Player receiver, final ChannelWrapper channelWrapper, final Object packet) {

					Object owner = null;

					if (receiver != null) {
						owner = receiver;
					} else {
						owner = channelWrapper;
					}

					final PacketEvent packetEvent = new PacketEvent(owner, packet);
					for (final PacketListener packetListener : packetListeners) {
						packetListener.out(packetEvent);
					}

					if (packetEvent.isCancelled())
						return null;

					return super.onPacketOutAsync(receiver, channelWrapper, packet);
				}
			};
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public boolean register(final PacketListener packetListener) {
		if(packetListeners.contains(packetListener))
			return false;
		packetListeners.add(packetListener);
		return true;
	}

	public boolean unregister(final Class<?> clazz) {
		final Iterator<PacketListener> listenerIterator = packetListeners.iterator();
		while (listenerIterator.hasNext()) {
			if (listenerIterator.next().getClass().equals(clazz)) {
				listenerIterator.remove();
				return true;
			}
		}
		return false;
	}

}
