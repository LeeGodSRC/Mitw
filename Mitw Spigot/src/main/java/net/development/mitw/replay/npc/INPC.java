package net.development.mitw.replay.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface INPC {

    void spawn(Location location, Player... players);

    void despawn();

    void animation();

    void teleport(Location location);

    void look(int yaw, int pitch);

    void animation(int id);

}
