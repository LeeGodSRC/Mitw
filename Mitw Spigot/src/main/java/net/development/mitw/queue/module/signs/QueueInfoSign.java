package net.development.mitw.queue.module.signs;

import net.development.mitw.queue.module.util.C;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueueInfoSign
{
    private static List<QueueInfoSign> signs;
    private static List<String> signLines;
    private String queue;
    private Location loc;
    
    public QueueInfoSign(final String queue, final Location loc) {
        this.queue = queue;
        this.loc = loc;
        QueueInfoSign.signs.add(this);
    }
    
    public static List<QueueInfoSign> getSigns(final String queue) {
        return QueueInfoSign.signs.stream().filter(s -> s.getQueue().equalsIgnoreCase(queue)).collect(Collectors.toList());
    }
    
    public static QueueInfoSign getSign(final Location loc) {
        return QueueInfoSign.signs.stream().filter(s -> s.getLoc().equals((Object)loc)).findAny().orElse(null);
    }
    
    public static boolean isSign(final Location loc) {
        return getSign(loc) != null;
    }
    
    public void update(final String rank, final int amount) {
        final Sign sign = (Sign)this.loc.getBlock().getState();
        for (int i = 0; i < QueueInfoSign.signLines.size(); ++i) {
            final String line = QueueInfoSign.signLines.get(i);
            if (line.contains("%" + rank + "%") || line.contains("%server%")) {
                sign.setLine(i, this.getFormattedString(line, rank, amount));
            }
            else if (!line.contains("%")) {
                sign.setLine(i, this.getFormattedString(line, rank, amount));
            }
        }
        sign.update();
    }
    
    private String getFormattedString(final String str, final String rank, final int amount) {
        return C.translate(str.replaceAll("%" + rank + "%", amount + "").replaceAll("%server%", this.queue));
    }
    
    public String getQueue() {
        return this.queue;
    }
    
    public Location getLoc() {
        return this.loc;
    }
    
    public static List<QueueInfoSign> getSigns() {
        return QueueInfoSign.signs;
    }
    
    public static List<String> getSignLines() {
        return QueueInfoSign.signLines;
    }
    
    static {
        QueueInfoSign.signs = new ArrayList<QueueInfoSign>();
        QueueInfoSign.signLines = new ArrayList<String>();
    }
}
