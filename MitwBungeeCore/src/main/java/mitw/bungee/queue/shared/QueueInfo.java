package mitw.bungee.queue.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueueInfo
{
    private static List<QueueInfo> queues;
    private String queue;
    private String inQueueString;
    private int online;
    private int max;
    
    static {
        QueueInfo.queues = new ArrayList<QueueInfo>();
    }
    
    public QueueInfo(final String queue) {
        this.inQueueString = "";
        this.online = 0;
        this.max = 0;
        this.queue = queue;
        QueueInfo.queues.add(this);
    }
    
    public static QueueInfo getQueueInfo(final String queue) {
        return QueueInfo.queues.stream().filter(q -> q.getQueue().equalsIgnoreCase(queue)).findAny().orElse(null);
    }
    
    public static String getQueue(final String player) {
        for (final QueueInfo info : QueueInfo.queues) {
            final Integer position = info.getPlayersInQueue().get(player);
            if (position != null) {
                return info.getQueue();
            }
        }
        return null;
    }
    
    public static int getPosition(final String player) {
        for (final QueueInfo info : QueueInfo.queues) {
            final Integer position = info.getPlayersInQueue().get(player);
            if (position != null) {
                return position;
            }
        }
        return -1;
    }
    
    public static QueueInfo deserialize(final String str) {
        final String[] args = str.split(",");
        final String queue = args[0];
        final String players = args[1];
        final int max = Integer.parseInt(args[2]);
        final int online = Integer.parseInt(args[3]);
        QueueInfo info = getQueueInfo(queue);
        if (info == null) {
            info = new QueueInfo(queue);
        }
        info.setInQueueString(players);
        info.setMax(max);
        info.setOnline(online);
        return info;
    }
    
    public Map<String, Integer> getPlayersInQueue() {
        final Map<String, Integer> inQueue = new HashMap<String, Integer>();
        if (this.inQueueString.isEmpty()) {
            return inQueue;
        }
        String[] split;
        for (int length = (split = this.inQueueString.split("~")).length, i = 0; i < length; ++i) {
            final String info = split[i];
            inQueue.put(info.split("=")[0], Integer.valueOf(info.split("=")[2]));
        }
        return inQueue;
    }
    
    public Map<Rank, Integer> getRanksInQueue() {
        final Map<Rank, Integer> inQueue = new HashMap<Rank, Integer>();
        if (this.inQueueString.isEmpty()) {
            return inQueue;
        }
        String[] split;
        for (int length = (split = this.inQueueString.split("~")).length, i = 0; i < length; ++i) {
            final String info = split[i];
            final Rank rank = Rank.getRank(info.split("=")[1]);
            if (rank != null) {
                if (inQueue.containsKey(rank)) {
                    inQueue.put(rank, inQueue.get(rank) + 1);
                }
                else {
                    inQueue.put(rank, 1);
                }
            }
        }
        return inQueue;
    }
    
    public int getRankCount(final Rank rank) {
        final Integer count = this.getRanksInQueue().get(rank);
        return (count == null) ? 0 : count;
    }
    
    public int getSize() {
        return this.getPlayersInQueue().size();
    }
    
    public String serialize() {
        return String.valueOf(this.queue) + "," + this.inQueueString + "," + this.max + "," + this.online;
    }
    
    public static List<QueueInfo> getQueues() {
        return QueueInfo.queues;
    }
    
    public String getQueue() {
        return this.queue;
    }
    
    public String getInQueueString() {
        return this.inQueueString;
    }
    
    public int getOnline() {
        return this.online;
    }
    
    public int getMax() {
        return this.max;
    }
    
    public void setQueue(final String queue) {
        this.queue = queue;
    }
    
    public void setInQueueString(final String inQueueString) {
        this.inQueueString = inQueueString;
    }
    
    public void setOnline(final int online) {
        this.online = online;
    }
    
    public void setMax(final int max) {
        this.max = max;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueueInfo)) {
            return false;
        }
        final QueueInfo other = (QueueInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$queue = this.getQueue();
        final Object other$queue = other.getQueue();
        Label_0065: {
            if (this$queue == null) {
                if (other$queue == null) {
                    break Label_0065;
                }
            }
            else if (this$queue.equals(other$queue)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$inQueueString = this.getInQueueString();
        final Object other$inQueueString = other.getInQueueString();
        if (this$inQueueString == null) {
            if (other$inQueueString == null) {
                return this.getOnline() == other.getOnline() && this.getMax() == other.getMax();
            }
        }
        else if (this$inQueueString.equals(other$inQueueString)) {
            return this.getOnline() == other.getOnline() && this.getMax() == other.getMax();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof QueueInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $queue = this.getQueue();
        result = result * 59 + (($queue == null) ? 43 : $queue.hashCode());
        final Object $inQueueString = this.getInQueueString();
        result = result * 59 + (($inQueueString == null) ? 43 : $inQueueString.hashCode());
        result = result * 59 + this.getOnline();
        result = result * 59 + this.getMax();
        return result;
    }
    
    @Override
    public String toString() {
        return "QueueInfo(queue=" + this.getQueue() + ", inQueueString=" + this.getInQueueString() + ", online=" + this.getOnline() + ", max=" + this.getMax() + ")";
    }
}
