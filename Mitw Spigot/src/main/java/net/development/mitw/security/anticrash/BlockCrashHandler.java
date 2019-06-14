package net.development.mitw.security.anticrash;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.development.mitw.config.AntiCrash;
import net.development.mitw.security.NBTType;
import spg.lgdev.handler.PacketHandler;
import spg.lgdev.iSpigot;

public class BlockCrashHandler {

    public BlockCrashHandler() {
        iSpigot.INSTANCE.addPacketHandler(new PacketHandler() {
            @Override
            public boolean handleSentPacket(PlayerConnection playerConnection, Packet packet) {
                return true;
            }

            @Override
            public boolean handleReceivedPacket(PlayerConnection playerConnection, Packet packet) {
                if (packet instanceof net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace) {
                    final Player player = playerConnection.player.getBukkitEntity();
                    final net.minecraft.server.v1_8_R3.ItemStack book = ((PacketPlayInBlockPlace) packet).getD();
                    if (book != null && book.hasTag() && book.getTag().hasKey("pages")) {
                        final net.minecraft.server.v1_8_R3.NBTTagList pages = book.getTag().getList("pages", NBTType.String.getType());
                        if (pages.size() > 50) {
                            handleInvalidPacket(player);
                            return false;
                        } else if (pages.size() >= 1) {
                            @SuppressWarnings("unused") final boolean isEvil = false;

                            for (int i = 0; i < pages.size(); i++) {
                                if (pages.getString(i).length() > 255) {
                                    handleInvalidPacket(player);
                                    return false;
                                }
                            }
                        }
                    }
                }
                return true;
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