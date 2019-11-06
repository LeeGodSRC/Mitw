package net.development.mitw.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.development.mitw.Mitw;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LobbiesUtil {

    private static String[] LOBBIES = {"waiting", "lobby-2", "lobby-3"};

    public static void sendToLobby(Player player) {
        List<String> aliveLobbies = Stream.of(LOBBIES).filter(Mitw.getInstance().getKeepAliveHandler()::isServerAlive).collect(Collectors.toList());
        if (aliveLobbies.size() == 0) {
            player.sendMessage("§c[WARNING] There is no lobbies available currently, try to use /play waiting to force join lobby, if you believe it's a bug please report.");
            return;
        }
        String result = aliveLobbies.get(Mitw.getRandom().nextInt(aliveLobbies.size()));

        player.sendMessage(Mitw.getInstance().getCoreLanguage().translate(player, "sending_to", RV.o("{0}", result)));

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(result);
        player.sendPluginMessage(Mitw.getInstance(), "BungeeCord", out.toByteArray());
    }

}
