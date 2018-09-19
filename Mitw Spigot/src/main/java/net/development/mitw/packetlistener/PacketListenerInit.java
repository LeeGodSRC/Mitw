package net.development.mitw.packetlistener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.development.mitw.utils.reflection.resolver.FieldResolver;
import net.development.mitw.utils.reflection.resolver.MemberResolver;
import net.development.mitw.utils.reflection.resolver.MethodResolver;
import net.development.mitw.utils.reflection.resolver.minecraft.NMSClassResolver;
import net.development.mitw.utils.reflection.util.AccessUtil;

public abstract class PacketListenerInit {

	protected static final ExecutorService addChannelExecutor = Executors.newSingleThreadExecutor();
	protected static final ExecutorService removeChannelExecutor = Executors.newSingleThreadExecutor();

	protected static final NMSClassResolver nmsClassResolver = new NMSClassResolver();

	static final Class<?> EntityPlayer     = nmsClassResolver.resolveSilent("EntityPlayer");
	static final Class<?> PlayerConnection = nmsClassResolver.resolveSilent("PlayerConnection");
	static final Class<?> NetworkManager   = nmsClassResolver.resolveSilent("NetworkManager");
	static final Class<?> Packet           = nmsClassResolver.resolveSilent("Packet");
	static final Class<?> ServerConnection = nmsClassResolver.resolveSilent("ServerConnection");
	static final Class<?> MinecraftServer  = nmsClassResolver.resolveSilent("MinecraftServer");

	static final FieldResolver entityPlayerFieldResolver     = new FieldResolver(EntityPlayer);
	static final FieldResolver playerConnectionFieldResolver = new FieldResolver(PlayerConnection);
	protected static final FieldResolver networkManagerFieldResolver   = new FieldResolver(NetworkManager);
	static final FieldResolver minecraftServerFieldResolver  = new FieldResolver(MinecraftServer);
	static final FieldResolver serverConnectionFieldResolver = new FieldResolver(ServerConnection);

	protected static final Field networkManager   = playerConnectionFieldResolver.resolveSilent("networkManager");
	protected static final Field playerConnection = entityPlayerFieldResolver.resolveSilent("playerConnection");
	static final Field serverConnection = minecraftServerFieldResolver.resolveByFirstTypeSilent(ServerConnection);
	static final Field connectionList   = serverConnectionFieldResolver.resolveByLastTypeSilent(List.class);

	static final MemberResolver<Method> craftServerFieldResolver = new MethodResolver(Bukkit.getServer().getClass());

	static Method getServer;

	protected static final String KEY_HANDLER = "packet_handler";
	protected static final String KEY_LISTENER_PLAYER = "packet_listener_player";
	protected static final String KEY_SERVER  = "packet_listener_server";

	public interface IListenerList<E> extends List<E> {}

	public interface IChannelHandler {}

	public interface IChannelWrapper {}

	public abstract void init(Player player);

	public abstract void remove(Player player);

	public abstract IListenerList newListenerList();

	public void addServerChannel() {
		try {
			final Object dedicatedServer = getServer.invoke(Bukkit.getServer());
			if (dedicatedServer == null) { return; }
			final Object serverConnection = PacketListenerInit.serverConnection.get(dedicatedServer);
			if (serverConnection == null) { return; }
			final List currentList = (List<?>) connectionList.get(serverConnection);
			final Field superListField = AccessUtil.setAccessible(currentList.getClass().getSuperclass().getDeclaredField("list"));
			final Object list = superListField.get(currentList);
			if (IListenerList.class.isAssignableFrom(list.getClass())) { return; }
			final List newList = Collections.synchronizedList(newListenerList());
			for (final Object o : currentList) {
				newList.add(o);
			}
			connectionList.set(serverConnection, newList);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	static {
		try {
			getServer = Bukkit.getServer().getClass().getDeclaredMethod("getServer");
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

}
