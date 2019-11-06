package net.development.mitw.queue.module.signs;

import net.development.mitw.queue.module.util.C;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueueJoinSign
{
    private static List<QueueJoinSign> signs;
    private static List<String> signLines;
    private String queue;
    private Location loc;
    
    public QueueJoinSign(final String queue, final Location loc) {
        this.queue = queue;
        this.loc = loc;
        QueueJoinSign.signs.add(this);
    }
    
    public static List<QueueJoinSign> getSigns(final String queue) {
        return QueueJoinSign.signs.stream().filter(s -> s.getQueue().equalsIgnoreCase(queue)).collect(Collectors.toList());
    }
    
    public static QueueJoinSign getSign(final Location loc) {
        return QueueJoinSign.signs.stream().filter(s -> s.getLoc().equals((Object)loc)).findAny().orElse(null);
    }
    
    public static boolean isSign(final Location loc) {
        return getSign(loc) != null;
    }
    
    public void update(final int online, final int max) {
        final Sign sign = (Sign)this.loc.getBlock().getState();
        for (int i = 0; i < QueueJoinSign.signLines.size(); ++i) {
            final String line = this.getFormattedString(QueueJoinSign.signLines.get(i), online, max);
            sign.setLine(i, line);
        }
        sign.update();
    }
    
    private String getFormattedString(final String str, final int online, final int max) {
        return C.translate(str.replaceAll("%server%", this.queue).replaceAll("%online%", online + "").replaceAll("%limit%", max + ""));
    }
    
    public String getQueue() {
        return this.queue;
    }
    
    public Location getLoc() {
        return this.loc;
    }
    
    public static List<QueueJoinSign> getSigns() {
        return QueueJoinSign.signs;
    }
    
    public static List<String> getSignLines() {
        return QueueJoinSign.signLines;
    }
    
    static {
        QueueJoinSign.signs = new ArrayList<QueueJoinSign>();
        QueueJoinSign.signLines = new ArrayList<String>();
    }
}
