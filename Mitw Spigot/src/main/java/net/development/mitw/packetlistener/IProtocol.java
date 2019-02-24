package net.development.mitw.packetlistener;

import org.bukkit.entity.Player;

import net.development.mitw.packetlistener.channel.ChannelWrapper;

public abstract class IProtocol {

	public interface IChannelWrapper {}

	public abstract Object onPacketOutAsync(Player sender, ChannelWrapper channelWrapper, Object packet);

	public abstract Object onPacketInAsync(Player sender, ChannelWrapper channelWrapper, Object packet);

}