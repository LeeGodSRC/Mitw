package mitw.bungee.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

@Getter
@AllArgsConstructor
public class PlayerEntryAddEvent extends Event {

    private final ProxiedPlayer player;
    private final String entry;

}
