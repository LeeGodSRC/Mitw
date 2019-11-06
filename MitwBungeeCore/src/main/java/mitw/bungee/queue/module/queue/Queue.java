package mitw.bungee.queue.module.queue;

import mitw.bungee.queue.module.QueueManager;
import mitw.bungee.queue.module.QueueMessages;
import mitw.bungee.queue.shared.Rank;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Queue
{
    private static List<Queue> queues;
    private String name;
    private ServerInfo info;
    private int limit;
    private double sendDelay;
    private long lastSend;
    private boolean paused;
    private LinkedList<QueuedPlayer> queued;
    
    static {
        Queue.queues = new ArrayList<Queue>();
    }
    
    public Queue(final String name, final ServerInfo info, final int limit, final double sendDelay) {
        this.sendDelay = 0.0;
        this.paused = false;
        this.queued = new LinkedList<QueuedPlayer>();
        this.name = name;
        this.info = info;
        this.limit = limit;
        this.sendDelay = sendDelay;
        Queue.queues.add(this);
    }
    
    public static Queue getQueue(final String name) {
        return Queue.queues.stream().filter(q -> q.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }
    
    public static Queue getQueue(final ProxiedPlayer player) {
        return Queue.queues.stream().filter(q -> q.getQueued().stream().filter(qp -> qp.getPlayer().equals(player)).findAny().orElse(null) != null).findAny().orElse(null);
    }
    
    public static boolean inQueue(final ProxiedPlayer player) {
        return getQueue(player) != null;
    }
    
    public boolean add(final ProxiedPlayer player, final Rank rank) {
        if (inQueue(player)) {
            final Queue queue = getQueue(player);
            player.sendMessage((BaseComponent) QueueMessages.getMsg().getAlreadyInMessage(queue.getInfo().getName(), queue.getPosition(player), queue.size()));
            return false;
        }
        final int index = this.findIndex(rank);
        this.queued.add(index, new QueuedPlayer(player, rank));
        player.sendMessage(QueueMessages.getMsg().getJoinMessage(player, this.info.getName(), this.getPosition(player), this.size()));
        return true;
    }
    
    public void remove(final ProxiedPlayer player) {
        final int index = this.getPosition(player) - 1;
        this.queued.remove(this.getQueued(player));
        player.sendMessage((BaseComponent) QueueMessages.getMsg().getLeaveMessage(player, this.info.getName()));
        if (QueueManager.getInstance().getConfig().isSendMoveMessage()) {
            this.sendPositionMessage(index, false);
        }
    }
    
    public void sendNext() {
        if (this.queued.isEmpty() || this.paused) {
            return;
        }
        this.lastSend = System.currentTimeMillis();
        final QueuedPlayer qp = this.queued.pop();
        this.sendPlayer(qp);
    }
    
    public void sendPlayer(final QueuedPlayer qp) {
        if (qp.isOnline()) {
            qp.getPlayer().connect(this.info);
        }
    }
    
    public boolean canSend() {
        return (this.limit == -1 || this.info.getPlayers().size() < this.limit) && System.currentTimeMillis() - this.lastSend > this.sendDelay * 1000.0;
    }
    
    public int size() {
        return this.queued.size();
    }
    
    public boolean isEmpty() {
        return this.queued.isEmpty();
    }
    
    public void sendPositionMessage(final int startIndex, final boolean movedBack) {
        for (int i = startIndex; i < this.size(); ++i) {
            final QueuedPlayer qp = this.queued.get(i);
            if (qp.getPlayer() != null && qp.getPlayer().isConnected()) {
                if (movedBack) {
                    qp.getPlayer().sendMessage((BaseComponent) QueueMessages.getMsg().getMoveDownMessage(qp.getPlayer(), this.info.getName(), this.getPosition(qp.getPlayer()), this.size()));
                }
                else {
                    qp.getPlayer().sendMessage((BaseComponent) QueueMessages.getMsg().getMoveUpMessage(qp.getPlayer(), this.info.getName(), this.getPosition(qp.getPlayer()), this.size()));
                }
            }
        }
    }
    
    public int getPosition(final ProxiedPlayer player) {
        for (int i = 0; i < this.size(); ++i) {
            if (this.queued.get(i).getPlayer().getName().equalsIgnoreCase(player.getName())) {
                return i + 1;
            }
        }
        return -1;
    }
    
    public QueuedPlayer getQueued(final ProxiedPlayer player) {
        return new ArrayList<QueuedPlayer>(this.queued).stream().filter(q -> q.getPlayer().equals(player)).findAny().orElse(null);
    }
    
    private int findIndex(final Rank rank) {
        if (this.isEmpty()) {
            return 0;
        }
        if (rank == null) {
            return this.size();
        }
        for (int i = 0; i < this.size(); ++i) {
            final QueuedPlayer qp = this.queued.get(i);
            if (qp.getRank() == null || qp.getRank().getPriority() < rank.getPriority()) {
                return i;
            }
        }
        return this.size();
    }
    
    public static List<Queue> getQueues() {
        return Queue.queues;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ServerInfo getInfo() {
        return this.info;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public double getSendDelay() {
        return this.sendDelay;
    }
    
    public long getLastSend() {
        return this.lastSend;
    }
    
    public boolean isPaused() {
        return this.paused;
    }
    
    public LinkedList<QueuedPlayer> getQueued() {
        return this.queued;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setInfo(final ServerInfo info) {
        this.info = info;
    }
    
    public void setLimit(final int limit) {
        this.limit = limit;
    }
    
    public void setSendDelay(final double sendDelay) {
        this.sendDelay = sendDelay;
    }
    
    public void setLastSend(final long lastSend) {
        this.lastSend = lastSend;
    }
    
    public void setPaused(final boolean paused) {
        this.paused = paused;
    }
    
    public void setQueued(final LinkedList<QueuedPlayer> queued) {
        this.queued = queued;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Queue)) {
            return false;
        }
        final Queue other = (Queue)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$info = this.getInfo();
        final Object other$info = other.getInfo();
        Label_0102: {
            if (this$info == null) {
                if (other$info == null) {
                    break Label_0102;
                }
            }
            else if (this$info.equals(other$info)) {
                break Label_0102;
            }
            return false;
        }
        if (this.getLimit() != other.getLimit()) {
            return false;
        }
        if (Double.compare(this.getSendDelay(), other.getSendDelay()) != 0) {
            return false;
        }
        if (this.getLastSend() != other.getLastSend()) {
            return false;
        }
        if (this.isPaused() != other.isPaused()) {
            return false;
        }
        final Object this$queued = this.getQueued();
        final Object other$queued = other.getQueued();
        if (this$queued == null) {
            if (other$queued == null) {
                return true;
            }
        }
        else if (this$queued.equals(other$queued)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Queue;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $info = this.getInfo();
        result = result * 59 + (($info == null) ? 43 : $info.hashCode());
        result = result * 59 + this.getLimit();
        final long $sendDelay = Double.doubleToLongBits(this.getSendDelay());
        result = result * 59 + (int)($sendDelay ^ $sendDelay >>> 32);
        final long $lastSend = this.getLastSend();
        result = result * 59 + (int)($lastSend ^ $lastSend >>> 32);
        result = result * 59 + (this.isPaused() ? 79 : 97);
        final Object $queued = this.getQueued();
        result = result * 59 + (($queued == null) ? 43 : $queued.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Queue(name=" + this.getName() + ", info=" + this.getInfo() + ", limit=" + this.getLimit() + ", sendDelay=" + this.getSendDelay() + ", lastSend=" + this.getLastSend() + ", paused=" + this.isPaused() + ", queued=" + this.getQueued() + ")";
    }
}
