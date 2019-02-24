package net.development.mitw.packetlistener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import lombok.Getter;
import net.development.mitw.Mitw;
import net.development.mitw.packetlistener.channel.ChannelWrapper;
import net.development.mitw.packetlistener.newer.Protocol_Newer;
import net.development.mitw.packetlistener.older.Protocol_Older;

public class PacketHandler {

	@Getter
	private final Set<PacketListener> packetListeners = new HashSet<>();

	@Getter
	private static PacketHandler instance;

	@Getter
	private IProtocol packetListenerInit;

	public PacketHandler() {
		instance = this;

		if (Reflection.VERSION.contains("7")) {
			try {
				packetListenerInit = new Protocol_Older(Mitw.getInstance()) {
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
							try {
								packetListener.in(packetEvent);
							} catch (final Exception e) { e.printStackTrace(); }
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
							try {
								packetListener.out(packetEvent);
							} catch (final Exception e) { e.printStackTrace(); }
						}

						if (packetEvent.isCancelled())
							return null;

						return super.onPacketOutAsync(receiver, channelWrapper, packet);
					}
				};
			} catch (final Exception e2) {
				e2.printStackTrace();
			}
			return;
		}

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
						try {
							packetListener.in(packetEvent);
						} catch (final Exception e) { e.printStackTrace(); }
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
						try {
							packetListener.out(packetEvent);
						} catch (final Exception e) { e.printStackTrace(); }
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
		if (true) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "------------------ADDED PACKETLISTENER #" + packetListener.getClass().getSimpleName());
		}
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
