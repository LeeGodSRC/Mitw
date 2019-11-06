package mitw.bungee.queue.shared;

import java.util.Arrays;

public enum BungeeChannel
{
    CREATE_QUEUE("CREATE_QUEUE", 0, "CreateQueue"), 
    JOIN_QUEUE("JOIN_QUEUE", 1, "JoinQueue"), 
    UPDATE_POSITIONS("UPDATE_POSITIONS", 2, "UpdatePositions");
    
    private String channel;
    
    public static BungeeChannel getChannel(final String channel) {
        return Arrays.asList(values()).stream().filter(v -> v.getChannel().equalsIgnoreCase(channel)).findAny().orElse(null);
    }
    
    public String getChannel() {
        return this.channel;
    }
    
    private BungeeChannel(final String s, final int n, final String channel) {
        this.channel = channel;
    }
}
