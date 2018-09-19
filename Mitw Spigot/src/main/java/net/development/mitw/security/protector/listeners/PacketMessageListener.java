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

import java.io.UnsupportedEncodingException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.development.mitw.Mitw;
import net.development.mitw.config.EzProtector;
import net.development.mitw.config.SimpleConfig;
import net.development.mitw.security.protector.MitwProtector;
import net.development.mitw.security.protector.mods.Schematica;
import net.development.mitw.security.protector.utils.ExecutionUtil;

public class PacketMessageListener implements PluginMessageListener {

    private final Mitw plugin;
    public PacketMessageListener(Mitw plugin) {
        this.plugin = plugin;
    }

    /**
     * Listen for plugin messages by various mods
     *
     * @param channel The plugin channel used by the mod.
     * @param player The player whose client sent the plugin message.
     * @param value The bytes included in the plugin message.
     */
    @Override
	public void onPluginMessageReceived(String channel, Player player, byte[] value) {
        MitwProtector.player = player.getName();
        final SimpleConfig config = EzProtector.getInstance();
        final ConsoleCommandSender console = Bukkit.getConsoleSender();

        if (config.getBoolean("mods.5zig.block")) block5Zig(player, channel);
        if (config.getBoolean("mods.bettersprinting.block")) blockBSM(player, channel);

        if (config.getBoolean("mods.schematica.block") && !player.hasPermission("ezprotector.bypass.mod.schematica")) {
            final byte[] payload = Schematica.getPayload();
            player.sendPluginMessage(plugin, MitwProtector.SCHEMATICA, payload);
        }

        if (channel.equalsIgnoreCase(MitwProtector.MCBRAND)) {
            // Converts the byte array to a string called "brand"
            String brand;
            try {
                brand = new String(value, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                throw new UnsupportedOperationException(e);
            }

            if (config.getBoolean("mods.forge.block")) blockForge(player, brand, config, console);
            if (config.getBoolean("mods.liteloader.block")) blockLiteLoader(player, brand, config, console);
        }
    }

    /**
     * Blocks the 5-Zig mod for a certain player.
     *
     * @param player The player to execute the block on.
     * @param channel The channel where the byte array should be sent.
     */
    private void block5Zig(Player player, String channel) {
        if (!player.hasPermission("ezprotector.bypass.mod.5zig") && (channel.equalsIgnoreCase(MitwProtector.ZIG)) || (channel.contains("5zig"))) {
            // Create a new data output
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            // Write bytes to the data output that tell 5Zig to block certain features
            out.writeByte(0x1 | 0x2 | 0x4 | 0x8 | 0x10 | 0x20 );
            // Send the data output as a byte array to the player
            player.sendPluginMessage(plugin, channel, out.toByteArray());
        }
    }

    /**
     * Blocks the BetterSprinting mod for a certain player.
     *
     * @param player The player to execute the block on.
     * @param channel The channel where the byte array should be sent.
     */
    private void blockBSM(Player player, String channel) {
        if (!player.hasPermission("ezprotector.bypass.mod.bettersprinting") && channel.equalsIgnoreCase(MitwProtector.BSM)) {
            // Create a new data output
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            // Write a byte to the data output to disable BSM
            out.writeByte(1);
            // Send the data output as a byte array to the player
            player.sendPluginMessage(plugin, channel, out.toByteArray());
        }
    }

    /**
     * Kicks a certain player if Forge is found on the client
     *
     * @param player The player to execute the block on.
     * @param brand The brand name recieved in the "MC|Brand" plugin message.
     * @param config The plugin configuration on the particular server.
     * @param console The server's console (where the punish command is executed)
     */
    private void blockForge(Player player, String brand, FileConfiguration config, ConsoleCommandSender console) {
        if (!player.hasPermission("ezprotector.bypass.mod.forge") && (brand.equalsIgnoreCase("fml,forge")) || (brand.contains("fml")) || (brand.contains("forge"))) {
            final String punishCommand = config.getString("mods.forge.punish-command");
            Bukkit.dispatchCommand(console, MitwProtector.placeholders(punishCommand));

            final String notifyMessage = config.getString("mods.forge.warning-message");
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.forge");
        }
    }

    /**
     * Kicks a certain player if LiteLoader is found on the client
     *
     * @param player The player to execute the block on.
     * @param brand The brand name recieved in the "MC|Brand" plugin message.
     * @param config The plugin configuration on the particular server.
     * @param console The server's console (where the punish command is executed)
     */
    private void blockLiteLoader(Player player, String brand, FileConfiguration config, ConsoleCommandSender console) {
        if (!player.hasPermission("ezprotector.bypass.mod.liteloader") && (brand.contains("Lite")) || (brand.equalsIgnoreCase("LiteLoader"))) {
            final String punishCommand = config.getString("mods.liteloader.punish-command");
            Bukkit.dispatchCommand(console, MitwProtector.placeholders(punishCommand));

            final String notifyMessage = config.getString("mods.liteloader.warning-message");
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.liteloader");
        }
    }

}
