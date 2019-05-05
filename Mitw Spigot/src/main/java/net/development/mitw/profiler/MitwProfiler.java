package net.development.mitw.profiler;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class MitwProfiler {

    private static Map<String, Long> profilers = new HashMap<>();

    public static void startSection(String section) {
        profilers.put(section, System.currentTimeMillis());
    }

    public static void stopSection(String section) {
        long time = profilers.remove(section);

        if (System.currentTimeMillis() - time > 100L) {
            Bukkit.broadcast("§csomething is taking too long to run! section: " + section, "mitw.admin");
        }
    }

}
