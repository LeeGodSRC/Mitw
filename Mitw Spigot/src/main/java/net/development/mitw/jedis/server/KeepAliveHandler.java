package net.development.mitw.jedis.server;

import net.development.mitw.Mitw;
import net.development.mitw.config.Settings;
import net.development.mitw.jedis.JedisPackets;
import net.development.mitw.json.JsonChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeepAliveHandler implements Runnable {

    public Map<String, Long> serverOnline = new ConcurrentHashMap<>();

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        serverOnline.entrySet().stream()
                .filter(entry -> time - entry.getValue() > 60000L)
                .forEach(entry -> serverOnline.remove(entry.getKey()));
        if (Settings.KEEP_ALIVE_PACKET) {
            Mitw.getInstance().getMitwJedis().write(JedisPackets.KEEP_ALIVE, new JsonChain()
                            .addProperty("server", Settings.KEEP_ALIVE_SERVER_NAME)
                            .addProperty("time", time).get());
        }
    }

    public void handleKeepAlive(String server, long keepAlive) {
        this.serverOnline.put(server, keepAlive);
    }

    public boolean isServerAlive(String server) {
        return this.serverOnline.containsKey(server);
    }
}
