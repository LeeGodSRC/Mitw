package net.development.mitw.security.anticrash;

import net.development.mitw.packetlistener.PacketEvent;
import net.development.mitw.packetlistener.PacketHandler;
import net.development.mitw.packetlistener.PacketListener;

public class BlockCrashHandler_18R3 /*implements BlockCrashHandler*/ {

	public BlockCrashHandler_18R3() {
		PacketHandler.getInstance().register(new PacketListener() {
			@Override
			public void out(PacketEvent packetEvent) {}
			@Override
			public void in(PacketEvent packetEvent) {
				// TODO Auto-generated method stub

			}
		});
	}

	/*	public BlockCrashHandler_18R3() {
		ProtocolLibrary.getProtocolManager()
				.addPacketListener((PacketListener) new PacketAdapter(Main.getInstance(), new PacketType[] { PacketType.Play.Client.BLOCK_PLACE }) {
					public void onPacketReceiving(final PacketEvent event) {
						final Player sender = event.getPlayer();

						if (event.getPacket().getItemModifier().read(0) != null) {
							if (event.getPacket().getItemModifier().read(0).getType().equals(Material.BOOK_AND_QUILL)) {
								ItemStack book = CraftItemStack.asNMSCopy(event.getPacket().getItemModifier().read(0));
								if (book.hasTag() && book.getTag().hasKey("pages")) {
									final NBTTagList pages = book.getTag().getList("pages", (int) NBTType.String.getType());
									if (pages.size() > 50) {
										event.setCancelled(true);
										handleInvalidPacket(sender);
									} else if (pages.size() >= 1) {
										@SuppressWarnings("unused")
										boolean isEvil = false;

										for (int i = 0; i < pages.size(); i++) {
											if (pages.getString(i).length() > 255) {
												event.setCancelled(true);
												handleInvalidPacket(sender);
												break;
											}
										}
									}
								}
							}
						}
					}
				});

	}

	public void handleInvalidPacket(Player player) {
		Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				try {
					if (player.isOnline()) {
						CommandSender cs = (CommandSender) Main.getInstance().getServer().getConsoleSender();
						for (String cmd : Main.getInstance().getConfigManager().getConfig().getStringList("anticrash.actions")) {
							cmd = ChatColor.translateAlternateColorCodes('&', cmd);
							cmd = cmd.replaceAll("%playername", player.getName());
							Main.getInstance().getServer().dispatchCommand(cs, cmd);
						}
					}
				} catch (NullPointerException e) {
				}
			}
		});
	}*/

}
