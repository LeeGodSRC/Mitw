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

import java.util.List;

import net.development.mitw.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.development.mitw.config.EzProtector;
import net.development.mitw.security.protector.MitwProtector;

public class HiddenSyntaxes {

    /**
     * Intercepts a command containing the ":" character and blocks it.
     *
     * @param event The command event from which other information is gathered.
     */
    public static void execute(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        MitwProtector.player = player.getName();
        final String command = event.getMessage();
        final Configuration config = EzProtector.getInstance();
        final ConsoleCommandSender console = Bukkit.getConsoleSender();

        // Get the commands which will not be filtered by this check
        final List<String> whitelisted = config.getStringList("hidden-syntaxes.whitelisted");

        // Check if the command contains :. If that is true, check if the player hasn't got the bypass permission and that the command hasn't got any spaces in it
        if (command.split(" ")[0].contains(":") && !player.hasPermission("ezprotector.bypass.command.hiddensyntax") && !whitelisted.contains(command.split(" ")[0].toLowerCase().replace("/", ""))) {
            event.setCancelled(true);
            // Replace placeholder with the executed command
            MitwProtector.playerCommand = command.replace("/", "");

            if (!MitwProtector.errorMessage.trim().equals("")) player.sendMessage(MitwProtector.placeholders(color(MitwProtector.errorMessage)));

            if (config.getBoolean("hidden-syntaxes.punish-player.enabled")) {
                final String punishCommand = config.getString("hidden-syntaxes.punish-player.command");
                // Replace placeholder with the error message in the config
                MitwProtector.errorMessage = config.getString("hidden-syntaxes.error-message");
                Bukkit.dispatchCommand(console, MitwProtector.placeholders(punishCommand));
            }

            if (config.getBoolean("hidden-syntaxes.notify-admins.enabled")) {
                final String notifyMessage = config.getString("hidden-syntaxes.notify-admins.message");
                ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.hiddensyntax");
            }
        }
    }

}
