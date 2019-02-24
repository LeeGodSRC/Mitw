/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.development.mitw.security.protector.listeners;

import static net.development.mitw.security.protector.utils.MessageUtil.color;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.development.mitw.config.EzProtector;
import net.development.mitw.config.SimpleConfig;
import net.development.mitw.packetlistener.PacketEvent;
import net.development.mitw.packetlistener.PacketHandler;
import net.development.mitw.packetlistener.PacketListener;
import net.development.mitw.security.protector.MitwProtector;
import net.development.mitw.security.protector.utils.ExecutionUtil;

public class PacketEventListener {

	public static void protocolLibHook() {
		final SimpleConfig config = EzProtector.getInstance();
		final List<String> blocked = config.getStringList("tab-completion.blacklisted");


		PacketHandler.getInstance().register(new PacketListener() {
			@Override
			public void out(final PacketEvent arg0) {}

			@Override
			public void in(final PacketEvent packetEvent) {
				if (packetEvent.getPacketName().equals("PacketPlayInTabComplate")) {

					if (!packetEvent.hasPlayer())
						return;

					final Player player = packetEvent.getPlayer();
					MitwProtector.player = player.getName();

					final String packetValue = (String) packetEvent.getPacketValue("a");

					if (packetValue == null)
						return;

					final String[] messages = packetValue.split(" ");

					if (messages.length == 0)
						return;

					final String message = messages[0].toLowerCase();

					for (final String command : blocked) {
						if (!player.hasPermission("mitw.admin")
								&& (message.equals(command) || (message.startsWith("/") && !message.contains(" ")))) {
							packetEvent.setCancelled(true);
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
							break;
						}
					}
				}
			}
		});
	}
}
