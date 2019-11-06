package mitw.bungee.queue.module.queue;

import mitw.bungee.queue.shared.Rank;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class QueuedPlayer
{
    private ProxiedPlayer player;
    private Rank rank;
    
    public void sendMessage(final String message) {
        this.player.sendMessage((BaseComponent)new TextComponent(message));
    }
    
    public boolean isOnline() {
        return this.player != null && this.player.isConnected();
    }
    
    public ProxiedPlayer getPlayer() {
        return this.player;
    }
    
    public Rank getRank() {
        return this.rank;
    }
    
    public QueuedPlayer(final ProxiedPlayer player, final Rank rank) {
        this.player = player;
        this.rank = rank;
    }
}
