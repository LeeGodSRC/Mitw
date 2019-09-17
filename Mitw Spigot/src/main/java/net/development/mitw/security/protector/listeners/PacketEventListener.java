package net.development.mitw.security.protector.listeners;

import static net.development.mitw.security.protector.utils.MessageUtil.color;

import java.util.List;

import net.development.mitw.config.Configuration;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.development.mitw.config.EzProtector;
import net.development.mitw.security.protector.MitwProtector;
import net.development.mitw.security.protector.utils.ExecutionUtil;
import spg.lgdev.protocol.listener.PacketController;
import spg.lgdev.protocol.listener.PacketDirection;
import spg.lgdev.protocol.listener.ProtocolVersions;

public class PacketEventListener {

    public static void protocolLibHook() {
        final Configuration config = EzProtector.getInstance();
        final List<String> blocked = config.getStringList("tab-completion.blacklisted");

        new PacketController<PacketPlayInTabComplete>(ProtocolVersions.V1_7_TO_1_8) {
            @Override
            public void init() {
                listen(values -> {
                    if (!values.isPlayer()) {
                        return;
                    }

                    final Player player = values.getPlayer();
                    MitwProtector.player = player.getName();

                    final String packetValue = values.getPacket().a();

                    if (packetValue == null)
                        return;

                    final String[] messages = packetValue.split(" ");

                    if (messages.length == 0)
                        return;

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
                            values.setCancel(true);
                        }
                    }
                });
            }
        }.start(PacketDirection.IN, PacketPlayInTabComplete.class);
    }
}