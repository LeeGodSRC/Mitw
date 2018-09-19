package net.development.mitw.packetlistener.channel;

import java.net.SocketAddress;

public class ChannelWrapper<T> {

	private final T channel;

	public ChannelWrapper(T channel) {
		this.channel = channel;
	}

	/**
	 * @return the raw channel object
	 */
	public T channel() {
		return this.channel;
	}

	/**
	 * @return the remote {@link SocketAddress}
	 */
	public SocketAddress getRemoteAddress() {
		return null;
	}

	/**
	 * @return the local {@link SocketAddress}
	 */
	public SocketAddress getLocalAddress() {
		return null;
	}

}
