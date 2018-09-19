/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.development.mitw.protector.mods;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.development.mitw.protector.MitwProtector;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class Schematica {

	/**
	 * Blocks the Schematica mod for a certain player (only used on 1.7.x)
	 *
	 * @param player
	 *            The player to execute the block on.
	 */
	public static void set(Player player) {
		if (!player.hasPermission("ezprotector.bypass.mod.schematica")
				&& MitwProtector.getPlugin().getServer().getVersion().contains("1.7")) {

			// JSON string that will be sent to the player
			final String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§0§2§0§0§e§f\"},{\"text\":\"§0§2§1§0§e§f\"},{\"text\":\"§0§2§1§1§e§f\"}]}";

			final PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(json));
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	/**
	 * Creates a byte array with options that tell Schematica which features to
	 * block (used for 1.8+)
	 *
	 * @return The byte array containing the Schematica block configuration.
	 */
	public static byte[] getPayload() {
		// Create the byte array and data stream
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try {
			// Add bytes to data stream
			dataOutputStream.writeByte(0);
			dataOutputStream.writeBoolean(false);
			dataOutputStream.writeBoolean(false);
			dataOutputStream.writeBoolean(false);

			// Convert the data stream to a byte array and return it
			return byteArrayOutputStream.toByteArray();
		} catch (final IOException ignored) {
			return null;
		}
	}
}
