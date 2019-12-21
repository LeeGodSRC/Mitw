package net.development.mitw.listener;

import lombok.AllArgsConstructor;
import net.development.mitw.Mitw;
import net.development.mitw.events.player.EntityDamageByPlayerEvent;
import net.development.mitw.events.player.PlayerDamageByEntityEvent;
import net.development.mitw.events.player.PlayerDamageByPlayerEvent;
import net.development.mitw.events.player.PlayerDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

@AllArgsConstructor
public class CallEventListener implements Listener {

    private final Mitw plugin;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
            if (damageByEntityEvent.getEntity() instanceof Player) {
                Player player = (Player) damageByEntityEvent.getEntity();
                if (damageByEntityEvent.getDamager() instanceof Player) {
                    plugin.getServer().getPluginManager().callEvent(new PlayerDamageByPlayerEvent(player, damageByEntityEvent));
                    return;
                }
                plugin.getServer().getPluginManager().callEvent(new PlayerDamageByEntityEvent(player, damageByEntityEvent));
                return;
            } else if (damageByEntityEvent.getDamager() instanceof Player) {
                Player player = (Player) damageByEntityEvent.getDamager();
                plugin.getServer().getPluginManager().callEvent(new EntityDamageByPlayerEvent(player, damageByEntityEvent));
            }
            return;
        }
        if (event.getEntity() instanceof Player) {
            plugin.getServer().getPluginManager().callEvent(new PlayerDamageEvent((Player) event.getEntity(), event));
        }
    }

}
