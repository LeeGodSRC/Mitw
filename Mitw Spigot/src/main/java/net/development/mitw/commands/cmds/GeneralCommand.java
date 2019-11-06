package net.development.mitw.commands.cmds;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.development.mitw.Mitw;
import net.development.mitw.commands.Command;
import net.development.mitw.commands.param.Parameter;
import net.development.mitw.utils.LobbiesUtil;
import org.bukkit.entity.Player;

public class GeneralCommand {

    @Command(names = {"play"})
    public static void play(Player player, @Parameter(name = "server") String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(Mitw.getInstance(), "BungeeCord", out.toByteArray());
    }

    @Command(names = {"lobby", "hub", "sendToHub"})
    public static void lobby(Player player) {
        LobbiesUtil.sendToLobby(player);
    }

}
