package mitw.bungee.jedis.server;

import com.sun.scenario.Settings;
import mitw.bungee.Mitw;

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
    }

    public void handleKeepAlive(String server, long keepAlive) {
        this.serverOnline.put(server, keepAlive);
    }

    public boolean isServerAlive(String server) {
        return this.serverOnline.containsKey(server);
    }
}
