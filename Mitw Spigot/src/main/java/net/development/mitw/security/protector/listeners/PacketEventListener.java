package net.development.mitw.security.protector.listeners;

import static net.development.mitw.security.protector.utils.MessageUtil.color;

import java.util.List;

import net.development.mitw.config.Configuration;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.development.mitw.config.EzProtector;
import net.development.mitw.security.protector.MitwProtector;
import net.development.mitw.security.protector.utils.ExecutionUtil;
import spg.lgdev.handler.PacketHandler;
import spg.lgdev.iSpigot;

public class PacketEventListener {

    public static void protocolLibHook() {
        final Configuration config = EzProtector.getInstance();
        final List<String> blocked = config.getStringList("tab-completion.blacklisted");


        iSpigot.INSTANCE.addPacketHandler(new PacketHandler() {
            @Override
            public boolean handleReceivedPacket(PlayerConnection playerConnection, Packet packet) {
                if (packet instanceof PacketPlayInTabComplete) {

                    final Player player = playerConnection.player.getBukkitEntity();
                    MitwProtector.player = player.getName();

                    final String packetValue = ((PacketPlayInTabComplete) packet).getA();

                    if (packetValue == null)
                        return true;

                    final String[] messages = packetValue.split(" ");

                    if (messages.length == 0)
                        return true;

                    final String message = messages[0].toLowerCase();

                    for (final String command : blocked) {
                        if (!player.hasPermission("mitw.admin")
                                && (message.equals(command) || (message.startsWith("/") && !message.contains(" ")))) {
                            if (config.getBoolean("tab-completion.warn.enabled")) {
                                final String errorMessage = config.getString("tab-completion.warn.message");
                                if (!errorMessage.trim().equals("")) {
                                    player.sendMessage(MitwProtector.placeholders(color(errorMessage)));
                                }
                            }

                            if (EzProtector.getInstance().getBoolean("tab-completion.punish-player.enabled")) {
                                final String punishCommand = config.getString("tab-completion.punish-player.command");
                                MitwProtector.errorMessage = config.getString("tab-completion.warn.message");
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                        MitwProtector.placeholders(punishCommand));
                            }

                            if (MitwProtector.getPlugin().getConfig()
                                    .getBoolean("tab-completion.notify-admins.enabled")) {
                                final String notifyMessage = config.getString("tab-completion.notify-admins.message");
                                ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.tabcomplete");
                            }
                            return false;
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean handleSentPacket(PlayerConnection playerConnection, Packet packet) {
                return true;
            }
        });
    }
}