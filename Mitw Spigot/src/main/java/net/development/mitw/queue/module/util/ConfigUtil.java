package net.development.mitw.queue.module.util;

import net.development.mitw.queue.module.QueueManager;
import net.development.mitw.queue.module.signs.QueueInfoSign;
import net.development.mitw.queue.module.signs.QueueJoinSign;
import net.development.mitw.queue.shared.Rank;
import org.bukkit.Location;

import java.util.Set;

public class ConfigUtil
{
    private QueueManager plugin;
    private String infoMsg;
    
    public ConfigUtil(final QueueManager plugin) {
        this.plugin = plugin;
    }
    
    public void load() {
        this.loadLines();
        this.loadSigns();
        this.loadRanks();
        this.infoMsg = C.translate(this.plugin.getConfig().getString("info-sign-message"));
    }
    
    public void saveSigns() {
        this.saveJoinSigns();
        this.saveInfoSigns();
    }
    
    public void loadSigns() {
        this.loadJoinSigns();
        this.loadInfoSigns();
    }
    
    public void loadRanks() {
        for (final String rank : this.plugin.getConfig().getConfigurationSection("ranks").getKeys(false)) {
            final String path = "ranks." + rank + ".";
            new Rank(rank, this.plugin.getConfig().getString(path + "permission"), this.plugin.getConfig().getInt(path + "priority"));
        }
    }
    
    public void saveJoinSigns() {
        final Set<String> keys = (Set<String>)this.plugin.getConfig().getConfigurationSection("queue-sign-locations").getKeys(false);
        for (final String sign : keys) {
            this.plugin.getConfig().set("queue-sign-locations." + sign, (Object)null);
        }
        for (int i = 0; i < QueueJoinSign.getSigns().size(); ++i) {
            final String path = "queue-sign-locations.sign" + i + ".";
            final QueueJoinSign sign2 = QueueJoinSign.getSigns().get(i);
            this.plugin.getConfig().set(path + "server", (Object)sign2.getQueue());
            this.plugin.getConfig().set(path + "location", (Object) EzQueueUtil.locToString(sign2.getLoc()));
        }
        this.plugin.getConfig().save();
    }
    
    public void saveInfoSigns() {
        final Set<String> keys = (Set<String>)this.plugin.getConfig().getConfigurationSection("info-sign-locations").getKeys(false);
        for (final String sign : keys) {
            this.plugin.getConfig().set("info-sign-locations." + sign, (Object)null);
        }
        for (int i = 0; i < QueueInfoSign.getSigns().size(); ++i) {
            final String path = "info-sign-locations.sign" + i + ".";
            final QueueInfoSign sign2 = QueueInfoSign.getSigns().get(i);
            this.plugin.getConfig().set(path + "server", (Object)sign2.getQueue());
            this.plugin.getConfig().set(path + "location", (Object) EzQueueUtil.locToString(sign2.getLoc()));
        }
        this.plugin.getConfig().save();
    }
    
    public void loadJoinSigns() {
        final Set<String> keys = (Set<String>)this.plugin.getConfig().getConfigurationSection("queue-sign-locations").getKeys(false);
        for (final String sign : keys) {
            System.out.println("found join sign " + sign);
            final String path = "queue-sign-locations." + sign + ".";
            final String server = this.plugin.getConfig().getString(path + "server");
            final Location loc = EzQueueUtil.stringToLoc(this.plugin.getConfig().getString(path + "location"));
            new QueueJoinSign(server, loc);
        }
    }
    
    public void loadInfoSigns() {
        final Set<String> keys = (Set<String>)this.plugin.getConfig().getConfigurationSection("info-sign-locations").getKeys(false);
        for (final String sign : keys) {
            final String path = "info-sign-locations." + sign + ".";
            final String server = this.plugin.getConfig().getString(path + "server");
            final Location loc = EzQueueUtil.stringToLoc(this.plugin.getConfig().getString(path + "location"));
            new QueueInfoSign(server, loc);
        }
    }
    
    public void loadLines() {
        for (final String key : this.plugin.getConfig().getConfigurationSection("queue-sign").getKeys(false)) {
            QueueJoinSign.getSignLines().add(this.plugin.getConfig().getString("queue-sign." + key));
        }
        for (final String key : this.plugin.getConfig().getConfigurationSection("info-sign").getKeys(false)) {
            QueueInfoSign.getSignLines().add(this.plugin.getConfig().getString("info-sign." + key));
        }
    }
    
    public String getInfoMsg() {
        return this.infoMsg;
    }
}
