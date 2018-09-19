/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.development.mitw.protector.utils;

import static net.development.mitw.protector.utils.MessageUtil.color;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.development.mitw.config.EzProtector;
import net.development.mitw.config.SimpleConfig;
import net.development.mitw.protector.MitwProtector;

public class CustomVersion {

    /**
     * Intercepts the /version command and swaps the output with a fake one
     *
     * @param event The command event from which other information is gathered.
     */
    public static void executeCustom(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String command = event.getMessage();

        final String[] ver = new String[]{"ver", "version"};
        if (!player.hasPermission("ezprotector.bypass.command.version")) {
            for (final String aList : ver) {
                // The command that is being tested at the moment
                MitwProtector.playerCommand = aList;
                if (command.split(" ")[0].equalsIgnoreCase("/" + MitwProtector.playerCommand)) {
                    event.setCancelled(true);
                    final String version = EzProtector.getInstance().getString("custom-version.version");
                    player.sendMessage(color("This server is running server version " + version));
                }
            }
        }
    }

    /**
     * Intercepts the /version command and blocks it for the player who executed it.
     *
     * @param event The command event from which other information is gathered.
     */
    public static void executeBlock(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        MitwProtector.player = player.getName();
        final SimpleConfig config = EzProtector.getInstance();
        final ConsoleCommandSender console = Bukkit.getConsoleSender();

        if (!player.hasPermission("ezprotector.bypass.command.version")) {
            final String[] ver = new String[]{"ver", "version"};
            for (final String aList : ver) {
                // The command that is being tested at the moment
                MitwProtector.playerCommand = aList;
                if (event.getMessage().split(" ")[0].equalsIgnoreCase("/" + MitwProtector.playerCommand)) {
                    event.setCancelled(true);
                    // Replace placeholder with the error message in the config
                    MitwProtector.errorMessage = config.getString("custom-version.error-message");

                    if (!MitwProtector.errorMessage.trim().equals("")) player.sendMessage(MitwProtector.placeholders(color(MitwProtector.errorMessage)));

                    if (config.getBoolean("custom-version.punish-player.enabled")) {
                        final String punishCommand = config.getString("custom-version.punish-player.command");
                        // Replace placeholder with the error message in the config
                        MitwProtector.errorMessage = config.getString("custom-version.error-message");
                        Bukkit.dispatchCommand(console, MitwProtector.placeholders(punishCommand));
                    }

                    if (config.getBoolean("custom-version.notify-admins.enabled")) {
                        final String notifyMessage = config.getString("custom-version.notify-admins.message");
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.version");
                    }
                }
            }
        }
    }

}
