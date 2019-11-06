package mitw.bungee.queue.module;

import mitw.bungee.Mitw;
import mitw.bungee.queue.module.util.C;
import mitw.bungee.queue.module.util.ConfigFile;
import mitw.bungee.queue.module.util.EzQueueUtil;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class QueueMessages
{
    private static QueueMessages msg;
    private String moveDownMessage;
    private String alreadyInMessage;
    private String availableQueuesMessage;
    private String invalidQueueMessage;
    private String alreadyConnectedMessage;
    private String notInQueueMessage;
    private String pauseQueueMessage;
    private String unpauseQueueMessage;
    private String queueInfoMessage;
    private boolean sendMoveMessage;
    private double sendMessageInterval;
    
    static {
        QueueMessages.msg = null;
    }
    
    public QueueMessages(final ConfigFile config) {
        this.moveDownMessage = C.translate(config.getString("messages.move-down-message"));
        this.alreadyInMessage = C.translate(config.getString("messages.already-in-message"));
        this.availableQueuesMessage = C.translate(config.getString("messages.available-queues-message"));
        this.invalidQueueMessage = C.translate(config.getString("messages.invalid-queue-message"));
        this.alreadyConnectedMessage = C.translate(config.getString("messages.already-connected-message"));
        this.notInQueueMessage = C.translate(config.getString("messages.not-in-queue-message"));
        this.pauseQueueMessage = C.translate(config.getString("messages.pause-queue-message"));
        this.unpauseQueueMessage = C.translate(config.getString("messages.unpause-queue-message"));
        this.queueInfoMessage = C.translate(config.getString("messages.queue-info-message"));
        this.sendMoveMessage = config.getBoolean("send-position-move-message");
        this.sendMessageInterval = config.getDouble("send-position-message-interval");
        QueueMessages.msg = this;
    }
    
    public TextComponent getJoinMessage(ProxiedPlayer player, final String server, final int position, final int outOf) {
        return new TextComponent(Mitw.INSTANCE.getLanguage().translate(player, "queue_join").replaceAll("%server%", server).replaceAll("%position%", new StringBuilder(String.valueOf(position)).toString()).replaceAll("%outof%", new StringBuilder(String.valueOf(outOf)).toString()));
    }
    
    public TextComponent getLeaveMessage(ProxiedPlayer player, final String server) {
        return new TextComponent(Mitw.INSTANCE.getLanguage().translate(player, "queue_leave").replaceAll("%server%", server));
    }
    
    public TextComponent getMoveUpMessage(ProxiedPlayer player, final String server, final int position, final int outOf) {
        return new TextComponent(Mitw.INSTANCE.getLanguage().translate(player, "queue_move_up").replaceAll("%server%", server).replaceAll("%position%", new StringBuilder(String.valueOf(position)).toString()).replaceAll("%outof%", new StringBuilder(String.valueOf(outOf)).toString()));
    }
    
    public TextComponent getMoveDownMessage(ProxiedPlayer player, final String server, final int position, final int outOf) {
        return new TextComponent(Mitw.INSTANCE.getLanguage().translate(player, "queue_move_down").replaceAll("%server%", server).replaceAll("%position%", new StringBuilder(String.valueOf(position)).toString()).replaceAll("%outof%", new StringBuilder(String.valueOf(outOf)).toString()));
    }
    
    public TextComponent getAlreadyInMessage(final String server, final int position, final int outOf) {
        return new TextComponent(this.alreadyInMessage.replaceAll("%server%", server).replaceAll("%position%", new StringBuilder(String.valueOf(position)).toString()).replaceAll("%outof%", new StringBuilder(String.valueOf(outOf)).toString()));
    }
    
    public TextComponent getAvailableQueuesMessage() {
        return new TextComponent(this.availableQueuesMessage.replaceAll("%servers%", EzQueueUtil.getQueues()));
    }
    
    public TextComponent getInvalidQueueMessage() {
        return new TextComponent(this.invalidQueueMessage.replaceAll("%servers%", EzQueueUtil.getServers()));
    }
    
    public TextComponent getPauseQueueMessage(final String server) {
        return new TextComponent(this.pauseQueueMessage.replaceAll("%server%", server));
    }
    
    public TextComponent getUnpauseQueueMessage(final String server) {
        return new TextComponent(this.unpauseQueueMessage.replaceAll("%server%", server));
    }
    
    public TextComponent getQueueInfoMessage(final String server, final int position, final int outOf) {
        return new TextComponent(this.queueInfoMessage.replaceAll("%server%", server).replaceAll("%position%", new StringBuilder(String.valueOf(position)).toString()).replaceAll("%outof%", new StringBuilder(String.valueOf(outOf)).toString()));
    }
    
    public TextComponent getAlreadyConnectedMessage() {
        return new TextComponent(this.alreadyConnectedMessage);
    }
    
    public TextComponent getNotInQueueMessage() {
        return new TextComponent(this.notInQueueMessage);
    }
    
    public static QueueMessages getMsg() {
        return QueueMessages.msg;
    }
    
    public boolean isSendMoveMessage() {
        return this.sendMoveMessage;
    }
    
    public double getSendMessageInterval() {
        return this.sendMessageInterval;
    }
}
