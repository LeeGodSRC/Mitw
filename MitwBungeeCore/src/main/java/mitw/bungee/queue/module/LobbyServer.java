package mitw.bungee.queue.module;

import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.List;

public class LobbyServer
{
    private static List<LobbyServer> lobbies;
    private String name;
    private ServerInfo info;
    
    static {
        LobbyServer.lobbies = new ArrayList<LobbyServer>();
    }
    
    public LobbyServer(final String name, final ServerInfo info) {
        this.name = name;
        this.info = info;
        LobbyServer.lobbies.add(this);
    }
    
    public static List<LobbyServer> getLobbies() {
        return LobbyServer.lobbies;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ServerInfo getInfo() {
        return this.info;
    }
}
