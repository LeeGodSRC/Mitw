/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.development.mitw.protector.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.development.mitw.config.EzProtector;
import net.development.mitw.config.SimpleConfig;
import net.development.mitw.protector.utils.CustomCommands;
import net.development.mitw.protector.utils.CustomPlugins;
import net.development.mitw.protector.utils.CustomVersion;
import net.development.mitw.protector.utils.HiddenSyntaxes;

public class CommandEventListener implements Listener {

    /**
     * Listener to intercept and check and commands executed by the player.
     * This runs before the actual command in question is executed.
     * If there is no issue, nothing happens, otherwise the command is blocked.
     *
     * @param event The command event from which other information is gathered.
     */
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
    	final SimpleConfig config = EzProtector.getInstance();

        if (config.getBoolean("custom-commands.blocked")) CustomCommands.execute(event);
        if (config.getBoolean("hidden-syntaxes.blocked")) HiddenSyntaxes.execute(event);

        // TODO: Merge some code from these two in one class - they are very similar!!
        if (config.getBoolean("custom-plugins.enabled")) CustomPlugins.executeCustom(event);
        else CustomPlugins.executeBlock(event);

        if (config.getBoolean("custom-version.enabled")) CustomVersion.executeCustom(event);
        else CustomVersion.executeBlock(event);
    }

}
