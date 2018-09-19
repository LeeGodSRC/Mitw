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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.development.mitw.config.EzProtector;
import net.development.mitw.config.SimpleConfig;
import net.development.mitw.security.protector.mods.DamageIndicators;
import net.development.mitw.security.protector.mods.ReiMinimap;
import net.development.mitw.security.protector.mods.Schematica;
import net.development.mitw.security.protector.mods.SmartMoving;

public class PlayerJoinListener implements Listener {

    /**
     * Sends out the mod blocks every time a player joins the server.
     *
     * @param event The join event from which other information is gathered.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player p = event.getPlayer();
        final SimpleConfig config = EzProtector.getInstance();

        if (config.getBoolean("mods.betterpvp.block") && !p.hasPermission("ezprotector.bypass.mod.betterpvp")) p.sendMessage(" §c §r§5 §r§1 §r§f §r§0 ");
        if (config.getBoolean("mods.damageindicators.block")) DamageIndicators.set(p);
        if (config.getBoolean("mods.reiminimap.block")) ReiMinimap.set(p);
        if (config.getBoolean("mods.schematica.block")) Schematica.set(p);
        if (config.getBoolean("mods.smartmoving.block")) SmartMoving.set(p);
        if (config.getBoolean("mods.voxelmap.block") && !p.hasPermission("ezprotector.bypass.mod.voxelmap")) p.sendMessage(" §3 §6 §3 §6 §3 §6 §e ");
    }

}
