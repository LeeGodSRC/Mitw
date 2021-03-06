/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.development.mitw.security.protector.mods;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class DamageIndicators {

	/**
	 * Blocks the DamageIndicators mod for a certain player.
	 *
	 * @param player
	 *            The player to execute the block on.
	 */
	public static void set(Player player) {
		if (!player.hasPermission("ezprotector.bypass.mod.damageindicators")) {

			final String json = "{\"text\":\"\",\"extra\":[{\"text\":\"\\u00a70\\u00a70\\u00a7c\\u00a7d\\u00a7e\\u00a7f\"}]}";

			final PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(json));
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

}
