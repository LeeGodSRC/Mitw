package net.development.mitw.security.anticrash;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.development.mitw.config.AntiCrash;
import net.development.mitw.packetlistener.PacketEvent;
import net.development.mitw.packetlistener.PacketHandler;
import net.development.mitw.packetlistener.PacketListener;
import net.development.mitw.security.NBTType;

public class BlockCrashHandler {

	public BlockCrashHandler() {
		PacketHandler.getInstance().register(new PacketListener() {
			@Override
			public void out(final PacketEvent packetEvent) {
			}

			@Override
			public void in(final PacketEvent packetEvent) {
				if (packetEvent.getPacket() instanceof net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace) {
					final Player player = packetEvent.getPlayer();
					final net.minecraft.server.v1_8_R3.ItemStack book = (net.minecraft.server.v1_8_R3.ItemStack) packetEvent.getPacketValue("d");
					if (book != null && book.hasTag() && book.getTag().hasKey("pages")) {
						final net.minecraft.server.v1_8_R3.NBTTagList pages = book.getTag().getList("pages", NBTType.String.getType());
						if (pages.size() > 50) {
							packetEvent.setCancelled(true);
							handleInvalidPacket(player);
						} else if (pages.size() >= 1) {
							@SuppressWarnings("unused")
							final boolean isEvil = false;

							for (int i = 0; i < pages.size(); i++) {
								if (pages.getString(i).length() > 255) {
									packetEvent.setCancelled(true);
									handleInvalidPacket(player);
									break;
								}
							}
						}
					}
				}
			}
		});
	}

	public void handleInvalidPacket(final Player player) {
		new Runnable() {
			@Override
			public void run() {
				try {
					if (player.isOnline()) {
						final CommandSender cs = Bukkit.getServer().getConsoleSender();
						for (String cmd : AntiCrash.ACTIONS) {
							cmd = ChatColor.translateAlternateColorCodes('&', cmd);
							cmd = cmd.replaceAll("%playername", player.getName());
							Bukkit.getServer().dispatchCommand(cs, cmd);
						}
					}
				} catch (final NullPointerException e) {
				}
			}
		}.run();
	}

}
