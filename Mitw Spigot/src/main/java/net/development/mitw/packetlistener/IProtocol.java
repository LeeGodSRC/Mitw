package net.development.mitw.packetlistener;

import java.util.List;

import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import io.netty.channel.Channel;
import net.development.mitw.packetlistener.Reflection.FieldAccessor;
import net.development.mitw.packetlistener.Reflection.MethodInvoker;
import net.development.mitw.packetlistener.channel.ChannelWrapper;

public abstract class IProtocol {

	protected static final MethodInvoker getPlayerHandle = Reflection.getMethod("{obc}.entity.CraftPlayer", "getHandle");
	protected static final FieldAccessor<Object> getConnection = Reflection.getField("{nms}.EntityPlayer", "playerConnection", Object.class);
	protected static final FieldAccessor<Object> getManager = Reflection.getField("{nms}.PlayerConnection", "networkManager", Object.class);
	protected static final FieldAccessor<Channel> getChannel = Reflection.getField("{nms}.NetworkManager", Channel.class, 0);

	// Looking up ServerConnection
	protected static final Class<Object> minecraftServerClass = Reflection.getUntypedClass("{nms}.MinecraftServer");
	protected static final Class<Object> serverConnectionClass = Reflection.getUntypedClass("{nms}.ServerConnection");
	protected static final FieldAccessor<Object> getMinecraftServer = Reflection.getField("{obc}.CraftServer", minecraftServerClass, 0);
	protected static final FieldAccessor<Object> getServerConnection = Reflection.getField(minecraftServerClass, serverConnectionClass, 0);
	protected static final MethodInvoker getNetworkMarkers = Reflection.getTypedMethod(serverConnectionClass, null, List.class, serverConnectionClass);

	// Packets we have to intercept
	protected static final Class<?> PACKET_LOGIN_IN_START = Reflection.getMinecraftClass("PacketLoginInStart");
	protected static final FieldAccessor<GameProfile> getGameProfile = Reflection.getField(PACKET_LOGIN_IN_START, GameProfile.class, 0);

	public interface IChannelWrapper {}

	public abstract Object onPacketOutAsync(Player sender, ChannelWrapper channelWrapper, Object packet);

	public abstract Object onPacketInAsync(Player sender, ChannelWrapper channelWrapper, Object packet);

}
