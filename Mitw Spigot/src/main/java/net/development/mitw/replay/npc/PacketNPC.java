package net.development.mitw.replay.npc;

import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketNPC implements INPC {

    private int id;
    private String name;
    private UUID uuid;
    private Player[] watcher;
    private Location location;

    private boolean spawned;

    private PacketPlayOutNamedEntitySpawn spawnPacket;

    public PacketNPC(int id, String name, UUID uuid) {
        this.id = id;
        this.name = name;
        this.uuid = uuid;
        this.spawnPacket = new PacketPlayOutNamedEntitySpawn();
    }

    @Override
    public void spawn(Location location, Player... players) {
        this.watcher = players;
        this.location = location;

        spawnPacket.setC(MathHelper.floor(location.getX() * 32D));
        spawnPacket.setC(MathHelper.floor(location.getY() * 32D));
        spawnPacket.setC(MathHelper.floor(location.getZ() * 32D));
    }

    public void setPosition(Location location) {
        if (!spawned) {
            spawnPacket.setC(MathHelper.floor(location.getX() * 32D));
            spawnPacket.setD(MathHelper.floor(location.getY() * 32D));
            spawnPacket.setE(MathHelper.floor(location.getZ() * 32D));
        } else {

        }
    }
}
