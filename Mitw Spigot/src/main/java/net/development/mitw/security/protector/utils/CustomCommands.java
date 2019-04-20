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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.development.mitw.config.EzProtector;
import net.development.mitw.security.protector.MitwProtector;

public class CustomCommands {

    /**
     * Intercepts a command if it's found to be blocked by the server admin.
     *
     * @param event The command event from which other information is gathered.
     */
    public static void execute(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        MitwProtector.player = player.getName();
        final String command = event.getMessage();
        final Configuration config = EzProtector.getInstance();
        final ConsoleCommandSender console = Bukkit.getConsoleSender();

        for (int i = 0; i < config.getList("custom-commands.commands").size(); i++) {
            // Replace placeholder with the command executed by the player
            MitwProtector.playerCommand = config.getList("custom-commands.commands").get(i).toString();
            if (((command.split(" ")[0].equalsIgnoreCase("/" + MitwProtector.playerCommand))) && !player.hasPermission("ezprotector.bypass.command.custom")) {
                event.setCancelled(true);
                // Replace placeholder with the error message in the config
                MitwProtector.errorMessage = config.getString("custom-commands.error-message");

                if (!MitwProtector.errorMessage.trim().equals("")) player.sendMessage(MitwProtector.placeholders(color(MitwProtector.errorMessage)));

                if (config.getBoolean("custom-commands.punish-player.enabled")) {
                    final String punishCommand = config.getString("custom-commands.punish-player.command");
                    // Replace placeholder with the error message in the config
                    MitwProtector.errorMessage = config.getString("tab-completion.warn.message");
                    Bukkit.dispatchCommand(console, MitwProtector.placeholders(punishCommand));
                }

                if (config.getBoolean("custom-commands.notify-admins.enabled")) {
                    final String notifyMessage = config.getString("custom-commands.notify-admins.message");
                    ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.custom");
                }
            }
        }
    }

}
