package net.development.mitw.packetlistener.newer;

import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.ArrayList;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.development.mitw.packetlistener.PacketEvent;
import net.development.mitw.packetlistener.PacketHandler;
import net.development.mitw.packetlistener.PacketListener;
import net.development.mitw.packetlistener.PacketListenerInit;
import net.development.mitw.packetlistener.channel.ChannelWrapper;

public class PacketIniter_Newer extends PacketListenerInit {

	private final Field channelField = networkManagerFieldResolver.resolveByFirstTypeSilent(io.netty.channel.Channel.class);

	@Override
	public void init(Player player) {
		try {
			final Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
			final ChannelDuplexHandler channelDuplexHandler = createChannel(player);
			addChannelExecutor.execute(() -> {
				channel.pipeline().addBefore(KEY_HANDLER, KEY_LISTENER_PLAYER, channelDuplexHandler);
			});
		} catch (final Exception e) {
			throw new RuntimeException("failed to add channcel to " + player.getName());
		}
	}

	@Override
	public void remove(Player player) {
		try {
			final Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
			removeChannelExecutor.execute(() -> {
				if(channel.pipeline().get(KEY_LISTENER_PLAYER) != null) {
					channel.pipeline().remove(KEY_LISTENER_PLAYER);
				}
			});
		} catch (final Exception e) {
			throw new RuntimeException("failed to add channcel to " + player.getName());
		}
	}

	public ChannelDuplexHandler createChannel(Player player) {
		return new ChannelHandler(player);
	}

	class ChannelHandler extends ChannelDuplexHandler implements IChannelHandler {

		private final Object owner;

		public ChannelHandler(Player player) {
			this.owner = player;
		}

		public ChannelHandler(ChannelWrapper channelWrapper) {
			this.owner = channelWrapper;
		}

		@Override
		public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
			if (PacketHandler.getInstance().getPacketListeners().isEmpty()) {
				super.channelRead(channelHandlerContext, packet);
				return;
			}
			final PacketEvent event = new PacketEvent(owner, packet);
			try {
				for (final PacketListener packetListener : PacketHandler.getInstance().getPacketListeners()) {
					packetListener.in(event);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			if (event.isCancelled()) {
				return;
			}
			super.channelRead(channelHandlerContext, packet);
		}

		@Override
		public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise)
				throws Exception {
			if (PacketHandler.getInstance().getPacketListeners().isEmpty()) {
				super.write(channelHandlerContext, packet, channelPromise);
				return;
			}
			final PacketEvent event = new PacketEvent(owner, packet);
			try {
				for (final PacketListener packetListener : PacketHandler.getInstance().getPacketListeners()) {
					packetListener.out(event);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			if (event.isCancelled()) {
				return;
			}
			super.write(channelHandlerContext, packet, channelPromise);
		}
	}

	@Override
	public IListenerList newListenerList() {
		return new ListenerList<>();
	}

	class ListenerList<E> extends ArrayList<E> implements IListenerList<E> {

		@Override
		public boolean add(E paramE) {
			try {
				final E a = paramE;
				addChannelExecutor.execute(() -> {
					try {
						io.netty.channel.Channel channel = null;
						while (channel == null) {
							channel = (io.netty.channel.Channel) channelField.get(a);
						}
						if (channel.pipeline().get(KEY_SERVER) == null) {
							channel.pipeline().addBefore(KEY_HANDLER, KEY_SERVER, new ChannelHandler(new INCChannelWrapper(channel)));
						}
					} catch (final Exception e) {
					}
				});
			} catch (final Exception e) {
			}
			return super.add(paramE);
		}

		@Override
		public boolean remove(Object arg0) {
			try {
				final Object a = arg0;
				removeChannelExecutor.execute(() -> {
					try {
						io.netty.channel.Channel channel = null;
						while (channel == null) {
							channel = (io.netty.channel.Channel) channelField.get(a);
						}
						channel.pipeline().remove(KEY_SERVER);
					} catch (final Exception e) {
					}
				});
			} catch (final Exception e) {
			}
			return super.remove(arg0);
		}
	}

	class INCChannelWrapper extends ChannelWrapper<io.netty.channel.Channel> implements IChannelWrapper {

		public INCChannelWrapper(io.netty.channel.Channel channel) {
			super(channel);
		}

		@Override
		public SocketAddress getRemoteAddress() {
			return this.channel().remoteAddress();
		}

		@Override
		public SocketAddress getLocalAddress() {
			return this.channel().localAddress();
		}
	}

}
