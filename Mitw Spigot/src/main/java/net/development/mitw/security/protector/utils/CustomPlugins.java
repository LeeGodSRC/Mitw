/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.development.mitw.security.protector.utils;

import static net.development.mitw.security.protector.utils.MessageUtil.color;

import net.development.mitw.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.development.mitw.config.EzProtector;
import net.development.mitw.security.protector.MitwProtector;

public class CustomPlugins {

    /**
     * Intercepts the /plugins command and swaps the output with a fake one
     *
     * @param event The command event from which other information is gathered.
     */
    public static void executeCustom(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String command = event.getMessage();

        final String[] plu = new String[]{"pl", "plugins"};
        if (!player.hasPermission("ezprotector.bypass.command.plugins")) {
            for (final String aList : plu) {
                // The command that is being tested at the moment
                MitwProtector.playerCommand = aList;
                if (command.split(" ")[0].equalsIgnoreCase("/" + MitwProtector.playerCommand)) {
                    event.setCancelled(true);

                    // Put all the fake plugins defined in a string array
                    StringBuilder defaultMessage = new StringBuilder("§a");
                    for (final String plugin : MitwProtector.plugins) defaultMessage.append(plugin).append(", ");
                    defaultMessage = new StringBuilder(defaultMessage.substring(0, defaultMessage.lastIndexOf(", ")));

                    // Create a fake /plugins output message using the string array above.
                    final String customPlugins = ChatColor.WHITE + "Plugins (" + MitwProtector.plugins.size() + "): " + ChatColor.GREEN + defaultMessage.toString().replaceAll(", ", String.valueOf(ChatColor.WHITE) + ", " + ChatColor.GREEN);

                    player.sendMessage(customPlugins);
                }
            }
        }
    }

    /**
     * Intercepts the /plugins command and blocks it for the player who executed it.
     *
     * @param event The command event from which other information is gathered.
     */
    public static void executeBlock(PlayerCommandPreprocessEvent event) {
    	final Configuration config = EzProtector.getInstance();
        final Player player = event.getPlayer();
        MitwProtector.player = player.getName();
        final ConsoleCommandSender console = Bukkit.getConsoleSender();

        if (!player.hasPermission("ezprotector.bypass.command.plugins")) {
            final String[] plu = new String[]{"pl", "plugins"};
            for (final String aList : plu) {
                // The command that is being tested at the moment
                MitwProtector.playerCommand = aList;
                if (event.getMessage().split(" ")[0].equalsIgnoreCase("/" + MitwProtector.playerCommand)) {
                    event.setCancelled(true);
                    // Replace placeholder with the error message in the config
                    MitwProtector.errorMessage = config.getString("custom-plugins.error-message");

                    if (!MitwProtector.errorMessage.trim().equals("")) player.sendMessage(MitwProtector.placeholders(color(MitwProtector.errorMessage)));

                    if (config.getBoolean("custom-plugins.punish-player.enabled")) {
                        final String punishCommand = config.getString("custom-plugins.punish-player.command");
                        // Replace placeholder with the error message in the config
                        MitwProtector.errorMessage = config.getString("custom-version.error-message");
                        Bukkit.dispatchCommand(console, MitwProtector.placeholders(punishCommand));
                    }

                    if (config.getBoolean("custom-plugins.notify-admins.enabled")) {
                        final String notifyMessage = config.getString("custom-plugins.notify-admins.message");
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.plugins");
                    }
                }
            }
        }
    }

}
