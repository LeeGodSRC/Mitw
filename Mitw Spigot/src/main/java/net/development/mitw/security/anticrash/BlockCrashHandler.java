package net.development.mitw.security.anticrash;

import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.development.mitw.config.AntiCrash;
import net.development.mitw.security.NBTType;
import spg.lgdev.protocol.listener.PacketController;
import spg.lgdev.protocol.listener.PacketDirection;
import spg.lgdev.protocol.listener.ProtocolVersions;

public class BlockCrashHandler {

    public BlockCrashHandler() {
        new PacketController<PacketPlayInBlockPlace>(ProtocolVersions.V1_7_TO_1_8) {
            @Override
            public void init() {
                listen(values -> {
                    final Player player = values.getPlayerConnection().player.getBukkitEntity();
                    final net.minecraft.server.v1_8_R3.ItemStack book = values.getPacket().getD();
                    if (book != null && book.hasTag() && book.getTag().hasKey("pages")) {
                        final net.minecraft.server.v1_8_R3.NBTTagList pages = book.getTag().getList("pages", NBTType.String.getType());
                        if (pages.size() > 50) {
                            handleInvalidPacket(player);
                            values.setCancel(true);
                        } else if (pages.size() >= 1) {
                            @SuppressWarnings("unused") final boolean isEvil = false;

                            for (int i = 0; i < pages.size(); i++) {
                                if (pages.getString(i).length() > 255) {
                                    handleInvalidPacket(player);
                                    values.setCancel(true);
                                }
                            }
                        }
                    }
                });
            }
        }.start(PacketDirection.IN, PacketPlayInBlockPlace.class);
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