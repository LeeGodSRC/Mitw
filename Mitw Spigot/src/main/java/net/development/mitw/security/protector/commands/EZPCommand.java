/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.development.mitw.security.protector.commands;

import static net.development.mitw.security.protector.utils.MessageUtil.color;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import net.development.mitw.config.EzProtector;

public class EZPCommand extends BukkitCommand {

    public EZPCommand() {
		super("protector");
	}

	/**
     * Checks if the /ezp command was executed and handles command logic.
     *
     * @param sender The player who sent the command
     * @param command The command which was sent
     * @param label Pretty much command.getName(). Not used in the code below
     * @param args The arguments after the command (/command <args>)
     * @return true if the command got executed successfully, otherwise false
     */
    @Override
	public boolean execute(CommandSender sender, String command, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(color("&6/protector reload - Reloads the plugin configuration."));
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            EzProtector.getInstance().reload();
            sender.sendMessage(color(EzProtector.getInstance().getString("prefix")) + " The config was reloaded!");
            return true;
        }
        sender.sendMessage(color("&4You have typed an invalid argument. Type /ezp to see a list of available commands."));
        return true;
    }
}
