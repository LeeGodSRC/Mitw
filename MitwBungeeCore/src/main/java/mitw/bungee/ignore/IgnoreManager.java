package mitw.bungee.ignore;

import com.google.common.collect.HashMultimap;
import mitw.bungee.config.Configuration;
import mitw.bungee.config.impl.Config;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class IgnoreManager {

    private HashMultimap<UUID, String> ignored = HashMultimap.create();

    public boolean addIgnore(UUID uuid, String target) {
        target = target.toLowerCase();
        if (isIgnored(uuid, target)) {
            return false;
        }
        ignored.put(uuid, target);
        return true;
    }

    public boolean removeIgnore(UUID uuid, String target) {
        target = target.toLowerCase();
        if (!isIgnored(uuid, target)) {
            return false;
        }
        ignored.remove(uuid, target);
        return true;
    }

    public boolean isIgnored(UUID uuid, String target) {
        target = target.toLowerCase();
        return ignored.containsEntry(uuid, target) || isIgnoreAll(uuid);
    }

    public boolean isIgnoreAll(UUID uuid) {
        return ignored.containsEntry(uuid, "*");
    }

    public void load(UUID uuid, Configuration config) {
        for (String ignore : config.getStringList("ignores")) {
            ignored.put(uuid, ignore);
        }
    }

    public void saveAndClear(UUID uuid, Configuration config) {
        Set<String> ignores = this.clear(uuid);
        config.set("ignores", new ArrayList<>(ignores));
    }

    public Set<String> clear(UUID uuid) {
        return ignored.removeAll(uuid);
    }

}
