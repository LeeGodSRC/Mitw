package net.development.mitw.packetlistener;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.packetlistener.channel.ChannelWrapper;
import net.development.mitw.utils.reflection.ReflectionUtils;
import net.development.mitw.utils.reflection.resolver.FieldResolver;

@Getter
public class PacketEvent {

	private final Player player;
	@SuppressWarnings("rawtypes")
	private final ChannelWrapper channelWrapper;

	private final Object packet;
	@Setter
	private boolean cancelled;

	protected FieldResolver fieldResolver;

	@SuppressWarnings("rawtypes")
	public PacketEvent(final Object owner, final Object packet) {
		if (owner instanceof Player) {
			this.player = (Player) owner;
			this.channelWrapper = null;
		} else {
			this.channelWrapper = (ChannelWrapper) owner;
			this.player = null;
		}

		this.packet = packet;

		fieldResolver = new FieldResolver(packet.getClass());
	}

	public void setPacketValue(final String field, final Object value) {
		ReflectionUtils.setField(packet, field, value);
	}

	public Object getPacketValue(final String field) {
		try {
			return fieldResolver.resolve(field).get(packet);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T getPacketValue(final String field, final Class<T> cast) {
		return (T) getPacketValue(field);
	}

	public String getPacketName() {
		return packet.getClass().getSimpleName();
	}

	public boolean hasPlayer() {
		return player != null;
	}

	public boolean hasChannel() {
		return channelWrapper != null;
	}

}
