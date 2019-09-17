package net.development.mitw.profiler;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MitwProfiler {

    private static Map<String, Long> profilers = new ConcurrentHashMap<>();

    public static void startSection(String section) {
        profilers.put(section, System.currentTimeMillis());
    }

    public static void stopSection(String section) {
        long time = profilers.remove(section);

        long take = System.currentTimeMillis() - time;

        if (take > 100L) {
            Bukkit.broadcast("§csomething is taking too long to run! section: " + section + " time: " + take + "ms", "mitw.admin");
        }
    }

}
